def call(String name) {
    def workspace = WORKSPACE.replaceAll('/','\\\\')
    switch (name) {//local preliminary file operations
        case ["fis", "mm_nix"] :
            loadScript(place:'linux', name:'prepareFiles.sh')
            sh "./prepareFiles.sh"
            break
        case ["borland"] :
        if (SVN=='trunk') { bat "xcopy ${workspace}\\${SVN} ${workspace} /i /q /d /e & mkdir ${TARGET}"}
        else { bat "xcopy ${workspace}\\${VERSION} ${workspace} /i /q /d /e & mkdir ${TARGET}" }
        break
        default:
            println ("Default: fm, svn_nexus, etl_nexus, mm_java:mmcore")
    }
}
