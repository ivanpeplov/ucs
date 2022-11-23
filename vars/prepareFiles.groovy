def call(String name) {
    def workspace = WORKSPACE.replaceAll('/','\\\\')
    switch (name) {//local preliminary file operations
        case ["fis", "fis_util", "mm_nix"] :
            loadLinuxScript('prepareFiles.sh')
            sh "./prepareFiles.sh"
            break
        case ["tid_man", "mms_eod", "palmera"] :
            //bat "xcopy C:\\jenkins\\trunk ${workspace} /i /q /d /e"
            bat "xcopy ${workspace}\\${SVN}\\${VERSION} ${workspace} /i /q /d /e"
            bat "mkdir ${TARGET}"
            break
        case ["mm_win"] :
            bat "xcopy C:\\MicroModule ${workspace} /i /q /d /e"
            //bat "xcopy ${workspace}\\MicroModule ${workspace} /i /q /d /e"
            //bat "rename cyassl-3.2.0 cyassl"
            //bat "mkdir ${TARGET}"
        default:
            println ("Default: fm, svn_nexus, etl_nexus")
    }
}
