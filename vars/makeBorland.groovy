def call(String rt, String label) {
    dir (rt) {
        switch (label) {  //module selection
        case ("palmerauloade") : //PalmeraUloader building
        makePalmMms("${label}", "${bmp}")
        break
        case ("mmseod") : //mmsEOD building
        makePalmMms("${label}", "${bmp}") 
        break
        case ("bin") : 
        bpl.split(',').each { f -> bat "xcopy ${f} ${TARGET}" }
        break
        case ("cardlib") : //TID Manager building
        bat "make -f cardlib.mak & xcopy C:\\bpl\\*.bpl ${TARGET}"
        break
        case ("cardpro") :
        bat "make -f cardpro.mak & xcopy cardpro.exe ${TARGET} & xcopy cardpro.ini ${TARGET}"
        break
        case ("32") : 
        bpl.split(',').each { f -> bat "xcopy ${f} ${TARGET}" }
        break
        case ("form") :
            dir = listDirWin("${WORKSPACE}\\${rt}") - 'TEMPLATE'
            for (io in dir) {
                switch (io) {
                case ('PRINT.CFG') :
                bmp.split(',').each {f->bat "xcopy .\\${io}\\${f} ${TARGET}\\${rt}\\${io}\\"}
                break
                default :
                bat "cd ${io} & make -f ${io}.mak"
                bat "cd ${io} & xcopy ${io}.dll ${TARGET}\\${rt}\\${io}\\" } }      
        break
        }
    }
}