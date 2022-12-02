def call(String rt, String label) {
    dir (rt) {
        switch (label) {  //module selection
        case ("palmerauloade") : //PalmeraUloader building
            makeBCB("${label}", "${bmp}")
        break
        case ("mmseod") : //mmsEOD building
            makeBCB("${label}", "${bmp}")
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
            mak = dir.collect {it.toLowerCase()}
            for (int i = 0; i < dir.size(); i++) {
                switch (dir[i]) {
                case ('PRINT.CFG') :
                bmp.split(',').each 
                {f->bat "xcopy .\\${dir[i]}\\${f} ${TARGET}\\${rt}\\${dir[i]}\\"}
                break
                default :
                bat "cd ${dir[i]} & make -f ${mak[i]}.mak"
                bat "cd ${dir[i]} & xcopy ${mak[i]}.dll ${TARGET}\\${rt}\\${dir[i]}\\" }      
            }
        break
        }
    }
}