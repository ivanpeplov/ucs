def call(String name) {
    switch (name) {//local preliminary file operations
        case ["fis", "fis_util", "micro_mod"] :
            loadLinuxScript('prepFiles.sh')
            sh "./prepFiles.sh"
            break
        case ["tid_man", "mms_eod", "palmera"] :
            def workspace = WORKSPACE.replaceAll('/','\\\\')
            //bat "xcopy C:\\jenkins\\trunk ${workspace} /i /q /d /e"
            bat "xcopy ${workspace}\\${SVN}\\${VERSION} ${workspace} /i /q /d /e"
            bat "mkdir ${TARGET}"
            break
        default:
            println ("Default: fm, svn_nexus, etl_nexus")
    }
}
