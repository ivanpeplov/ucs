def call(String operation, String label) {
    dir (operation) {
        //module selection
        switch (label) {
        //PalmeraUloader building
        case ('palmera') :
            bat "make -f palmerauloade.mak & xcopy PalmeraULoade.exe ${TARGET}"
            bmp.split(',').each { filename -> bat "xcopy ${filename} ${TARGET}" }
        break
        //mmsEOD building
        case ('mms_eod') :
            bat "make -f mmseod.mak & xcopy mmsEOD.exe ${TARGET}"
            bmp.split(',').each { filename -> bat "xcopy ${filename} ${TARGET}" }
        break
        case ('bin') :
            bpl.split(',').each { filename -> bat "xcopy ${filename} ${TARGET}" }
        break
        //TID Manager building
        case ("lib") :
            mod1=operation.toLowerCase()
            bat "make -f ${mod1}.mak"
            bat "xcopy C:\\bpl\\*.bpl ${TARGET}"
        break
        case ('tid_man') :
            bat "make -f CardPro.mak & xcopy Cardpro.exe ${TARGET}"
            bat "xcopy Cardpro.ini ${TARGET}"
        break
        case ('print') :
            bmp.split(',').each { filename -> bat "xcopy ${filename} ${TARGET}\\${operation}\\"}
        break
        case ('32') :
            bpl.split(',').each { filename -> bat "xcopy ${filename} ${TARGET}" }
        break
        case ("form") :
            nomakList=['TEMPLATE', 'PRINT.CFG']
            def dir = listDirWin("${WORKSPACE}/${operation}")
            def makList = dir - nomakList
            def makList1 = makList.collect{ it.toLowerCase() }
            for (int i=0; i < makList.size(); i++) {
                bat "cd ${makList[i]} & make -f ${makList1[i]}.mak"
                bat "cd ${makList[i]} & xcopy ${makList1[i]}.dll ${TARGET}\\${operation}\\${makList[i]}\\"
            }  
        break
        default:
            println "TBD"       
        }
    }
}