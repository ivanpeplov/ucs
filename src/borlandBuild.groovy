def call(String workdir, String module) { //borland groovy
    dir (workdir) {
        switch (module) {  //module selection
            case ("form") : //cardpro
                dirList = listDir.Win("form") - 'TEMPLATE' - 'PRINT.CFG'
                for (io in dirList) {
                    bat "cd ${io} & make -f ${io}.mak & xcopy ${io}.dll ${TARGET}\\${workdir}\\${io}\\"
                }
            break
            default :
                bat "make -f ${module}.mak"
                if(module=='cardlib') {bat "xcopy C:\\bpl\\*.bpl ${TARGET}"}
                else { app_list.split(',').each { f -> bat "xcopy ${f} ${TARGET}"} }
        }
    }
}