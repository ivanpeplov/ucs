def call(String rt, String label) {
    dir (rt) {
        switch (label) {  //module selection
        //mmsEOD building
        case ("mmseod") :
        bat "make -f ${label}.mak & xcopy ${label}.exe ${TARGET}"
        bmp.split(',').each { f -> bat "xcopy ${f} ${TARGET}"}
        break
        case ("bin") : bpl.split(',').each { f -> bat "xcopy ${f} ${TARGET}" }; break;
        //PalmeraUloader building
        case ("palmerauloade") :
        bat "make -f ${label}.mak & xcopy ${label}.exe ${TARGET}"
        bmp.split(',').each { f -> bat "xcopy ${f} ${TARGET}"}
        break
        //PassKey building
        case ("passkey") : bat "make -f PassKey(repo).mak & xcopy ${label}.exe ${TARGET}"; break;
        case ("dll") : bmp.split(',').each { f -> bat "xcopy ${f} ${TARGET}"}; break;
        //TID Manager building
        case ("cardlib") : bat "make -f cardlib.mak & xcopy C:\\bpl\\*.bpl ${TARGET}"; break; 
        case ("32") : bpl.split(',').each { f -> bat "xcopy ${f} ${TARGET}" }; break;
        case ("cardpro") : bat "make -f cardpro.mak & xcopy cardpro.exe ${TARGET} & xcopy cardpro.ini ${TARGET}" ;  break;
        case ("form") :
            dir = listDir.Win("form") - 'TEMPLATE'
            for (io in dir) {
                if (io=='PRINT.CFG') { bmp.split(',').each {f->bat "xcopy .\\${io}\\${f} ${TARGET}\\${rt}\\${io}\\"} }
                else { bat "cd ${io} & make -f ${io}.mak & xcopy ${io}.dll ${TARGET}\\${rt}\\${io}\\" } }
        break
        }
    }
}