def call(String operation) {
    dir (operation) {
        //module selection
        switch (operation) {
        //PalmeraUloader building
        case ('C:\\jenkins\\workspace\\UCS\\palmera') :
            bat "make -f palmerauloade.mak & xcopy PalmeraULoade.exe ${TARGET}"
            bmp.split(',').each { filename -> bat "xcopy ${filename} ${TARGET}" }
        break
        //mmsEOD building
        case ('C:\\jenkins\\workspace\\UCS\\mms_eod') :
            bat "make -f mmseod.mak & xcopy mmsEOD.exe ${TARGET}"
            bmp.split(',').each { filename -> bat "xcopy ${filename} ${TARGET}" }
        break
        case ('C:\\Program Files\\Borland\\CBuilder6\\Bin') :
            bpl.split(',').each { filename -> bat "xcopy ${filename} ${TARGET}" }
        break
        //TID Manager building
        case ("CARDLIB") :
            mod1=operation.toLowerCase()
            bat "make -f ${mod1}.mak"
            bat "xcopy C:\\bpl\\*.bpl ${TARGET}"
        break
        case ('C:\\jenkins\\workspace\\UCS\\tid_man') :
            bat "make -f CardPro.mak & xcopy Cardpro.exe ${TARGET}"
            bat "xcopy Cardpro.ini ${TARGET}"
        break
        case ('FORM\\PRINT.CFG') :
            bmp.split(',').each { filename -> bat "xcopy ${filename} ${TARGET}\\${operation}\\"}
        break
        case ('C:\\Windows\\System32') :
            bpl.split(',').each { filename -> bat "xcopy ${filename} ${TARGET}" }
        break
        case ("FORM") :
            nomakList=['TEMPLATE', 'PRINT.CFG']
            def dir = listDir ("${WORKSPACE}/${operation}")
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