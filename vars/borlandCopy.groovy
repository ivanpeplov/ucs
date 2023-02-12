def call(String workdir, String module) { //borland groovy
    dir (workdir) {
        if (module=='print') {
            printCfg.split(',').each
            { file->bat "xcopy ${file} ${TARGET}\\${workdir}\\" } }
        else { lib.split(',').each { file -> bat "xcopy ${file} ${TARGET}" } }
    }
}