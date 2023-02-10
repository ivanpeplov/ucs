def call(String name) { //for all jenkinsfile
    def workspace = WORKSPACE.replaceAll('/','\\\\')
    switch (name) {//local preliminary file operations
        case ["fis", "mm_nix"] : //fis; mm_nix operations
            loadScript(place:'linux', name:'mmPrepareFiles.sh')
            sh "./mmPrepareFiles.sh"
        break
        case ["mm_win"] : //mm_win operations
            bat "xcopy .\\MicroModule .\\units /i /q /d /e"
        break
        case ["ppcfm"] : //fm_host operations
            sh """ 
            cp ./git/config/fmux/cfgbuild.mak ./FmUX
            cp ./git/config/fmux/Makefile ./FmUX/fm """
        break
        case ["borland"] : //borland operations
        if (SVN=='trunk' || SVN=='src') { 
            bat "xcopy .\\${SVN} . /i /q /d /e & mkdir ${TARGET}"
            if (SVN=='src') { 
            bat "copy c:\\PassKey\\*.dll . & move PassKey(repo).mak passkey.mak" } }
        else { 
            bat "xcopy .\\${VERSION} . /i /q /d /e & mkdir ${TARGET}" }
        break
        default: println ("Default: fm_host (armfm, pseutils, fmman); mm_win")
    }
}
