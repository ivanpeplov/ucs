def call(String name) {
    switch (name) {//local preliminary file operations
        case ["fis", "fis_util", "mm_nix"] :
            loadLinuxScript('prepareFiles.sh')
            sh "./prepareFiles.sh"
            break
        case ["tid_man", "mms_eod", "palmera", "mm_win"] :
            def workspace = WORKSPACE.replaceAll('/','\\\\')
            bat "xcopy C:\\MicroModule ${workspace} /i /q /d /e"
            bat "rename cyassl-3.2.0 cyassl"
            //bat "xcopy ${workspace}\\${TOOR} ${workspace} /i /q /d /e"
            //bat "mkdir ${TARGET}"
            break
        default:
            println ("Default: fm, svn_nexus, etl_nexus")
    }
}
