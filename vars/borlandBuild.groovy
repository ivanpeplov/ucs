def call(String workdir, String module) { //borland groovy
    dir (workdir) {
        switch (module) {  //module selection
            case ("form") : //cardpro
                dirList = listDir.Win("form") - 'TEMPLATE' - 'PRINT.CFG'
                dirList.each { d->
                    bat "cd ${d} & make -f ${d}.mak & xcopy ${d}.dll ${TARGET}\\${workdir}\\${d}\\" }
            break
            default :
                bat "make -f ${module}.mak"
                if(module=='cardlib') {bat "xcopy C:\\bpl\\*.bpl ${TARGET}"}
                else { app_list.split(',').each { file -> bat "xcopy ${file} ${TARGET}"} }
        }
    }
}