def call(String operation, String label) {
    dir (operation) {
        switch (label) {  //module selection
        case ('palmera') : //PalmeraUloader building
            bat "make -f palmerauloade.mak & xcopy PalmeraULoade.exe ${TARGET}"
            bmp.split(',').each { filename -> bat "xcopy ${filename} ${TARGET}" }
        break
        case ('mms_eod') : //mmsEOD building
            bat "make -f mmseod.mak & xcopy mmsEOD.exe ${TARGET}"
            bmp.split(',').each { filename -> bat "xcopy ${filename} ${TARGET}" }
        break
        case ('bin') :
            bpl.split(',').each { filename -> bat "xcopy ${filename} ${TARGET}" }
        break
        case ("lib") : //TID Manager building
            mod1=operation.toLowerCase()
            bat "make -f ${mod1}.mak"
            bat "xcopy C:\\bpl\\*.bpl ${TARGET}"
        break
        case ("form") :
            excl=['TEMPLATE', 'PRINT.CFG']
            dir = listDirWin("${WORKSPACE}\\${operation}")
            println dir
            dir = dir - excl
            mak = dir.collect{ it.toLowerCase() }
            println dir
            println mak
            for (int i = 0; i < dir.size(); i++) {
                bat "cd ${dir[i]} & make -f ${mak[i]}.mak"
                bat "cd ${dir[i]} & xcopy ${mak[i]}.dll ${TARGET}\\${operation}\\${dir[i]}\\" } 
        break
        case ('tid_man') :
            bat "make -f CardPro.mak & xcopy Cardpro.exe ${TARGET}"
            bat "xcopy Cardpro.ini ${TARGET}"
        break
        case ('32') :
            bpl.split(',').each { filename -> bat "xcopy ${filename} ${TARGET}" }
        break
        case ('print') :
            bmp.split(',').each { filename -> bat "xcopy ${filename} ${TARGET}\\${operation}\\"}
        break
        default: println "TBD"    
        }
    }
}