def call(String workdir, String module) { //borland groovy
    dir (workdir) {
        if (module=='print') {
            printCfg_list.split(',').each
            { f->bat "xcopy ${f} ${TARGET}\\${workdir}\\" } }
        else { bpl.split(',').each { f -> bat "xcopy ${f} ${TARGET}" } }
    }
}