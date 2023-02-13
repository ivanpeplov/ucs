def call(String workdir) { //borland.groovy
    dir (workdir) {
        if (workdir=='FORM\\PRINT.CFG') {
            printCfg.split(',').each
            { file->bat "xcopy ${file} ${TARGET}\\${workdir}\\" } }
        else { lib.split(',').each { file -> bat "xcopy ${file} ${TARGET}" } }
    }
}