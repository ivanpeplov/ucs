def call(String workdir, String module) { //borland groovy
    dir (workdir) {
        switch (module) {  //module selection
            case ["cardpro", "cardlib", "mmseod", "palmerauloade", "passkey"] : //all builds
                bat "make -f ${module}.mak"
                if(module=='cardlib') {bat "xcopy C:\\bpl\\*.bpl ${TARGET}"}
                else { app_list.split(',').each { f -> bat "xcopy ${f} ${TARGET}"} }
            break
            case ["bin", "32"] : //mmseod and cardpro
                bpl.split(',').each { f -> bat "xcopy ${f} ${TARGET}" }
            break
            case ("form") : //cardpro
                dir = listDir.Win("form") - 'TEMPLATE'
                for (io in dir) {
                    if (io=='PRINT.CFG') { printCfg_list.split(',').each
                    {f->bat "xcopy .\\${io}\\${f} ${TARGET}\\${workdir}\\${io}\\"} }
                    else 
                    { bat "cd ${io} & make -f ${io}.mak & xcopy ${io}.dll ${TARGET}\\${workdir}\\${io}\\" } 
                }
            break
        }
    }
}