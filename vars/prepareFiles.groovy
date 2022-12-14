def call(String name) {
    def workspace = WORKSPACE.replaceAll('/','\\\\')
    switch (name) {//local preliminary file operations
        case ["fis", "mm_nix"] :
            loadScript(place:'linux', name:'prepareFiles.sh')
            sh "./prepareFiles.sh"
            break
        case ["borland"] :
            bat "xcopy ${workspace}\\${SVN}\\${VERSION} ${workspace} /i /q /d /e & mkdir ${TARGET}"
        break
        default:
            println ("Default: fm, svn_nexus, etl_nexus, mm_java:mmcore")
    }
}
