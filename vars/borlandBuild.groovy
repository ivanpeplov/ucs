def call(String workdir, String module) { //borland groovy
    dir (workdir) {
        switch (module) {  //module selection
            case ["cardpro", "mmseod", "palmerauloade", "passkey"] : //all builds
                bat "make -f ${module}.mak & xcopy ${module}.exe ${TARGET}"
                bmp.split(',').each { f -> bat "xcopy ${f} ${TARGET}"}
            break
            case ["bin", "32"] : //mmseod and cardpro
                bpl.split(',').each { f -> bat "xcopy ${f} ${TARGET}" }
            break
            case ("cardlib") : //cardpro
                bat "make -f ${module}.mak & xcopy C:\\bpl\\*.bpl ${TARGET}"
            break 
            case ("form") : //cardpro
                dir = listDir.Win("form") - 'TEMPLATE'
                for (io in dir) {
                    if (io=='PRINT.CFG')
                    { frm.split(',').each {f->bat "xcopy .\\${io}\\${f} ${TARGET}\\${workdir}\\${io}\\"} }
                    else { bat "cd ${io} & make -f ${io}.mak & xcopy ${io}.dll ${TARGET}\\${workdir}\\${io}\\" } 
                }
            break
        }
    }
}