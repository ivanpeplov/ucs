def call(String workdir, String module) { //borland groovy
    dir (workdir) {
        if (module=='print') {
            printCfg_list.split(',').each
            { file->bat "xcopy ${file} ${TARGET}\\${workdir}\\" } }
        else { bpl.split(',').each { file -> bat "xcopy ${file} ${TARGET}" } }
    }
}