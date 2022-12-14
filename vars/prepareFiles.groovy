def call(String name) {
    def workspace = WORKSPACE.replaceAll('/','\\\\')
    switch (name) {//local preliminary file operations
        case ["fis", "mm_nix"] :
            loadScript(place:'linux', name:'prepareFiles.sh')
            sh "./prepareFiles.sh"
            break
        case ["borland"] :
        if (SVN=='trunk' || SVN=='src') 
        { bat "xcopy ${workspace}\\${SVN} ${workspace} /i /q /d /e & mkdir ${TARGET}"
          bat "copy c:\\PassKey\\src\\PassKey.mak ${workspace}" } //optional copy for test
        else { bat "xcopy ${workspace}\\${VERSION} ${workspace} /i /q /d /e & mkdir ${TARGET}" }
        break
        default: println ("Default: fm, svn_nexus, etl_nexus, mm_java:mmcore")
    }
}
