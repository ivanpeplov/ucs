def call(String name) {
    def workspace = WORKSPACE.replaceAll('/','\\\\')
    switch (name) {//local preliminary file operations
        case ["fis", "mm_nix"] : //fis; mm_nix operations
            loadScript(place:'linux', name:'mmPrepareFiles.sh')
            sh "./mmPrepareFiles.sh"
        break
        case ["mm_win"] : //mm_win operations
            bat "xcopy ${workspace}\\MicroModule ${workspace}\\units /i /q /d /e"
        break
        case ["ppcfm"] : //fm_host operations
            sh """ 
            cp ./git/config/fmux/cfgbuild.mak ./FmUX
            cp ./git/config/fmux/Makefile ./FmUX/fm """
        break
        case ["borland"] : //borland operations
        if (SVN=='trunk' || SVN=='src')
        { bat "xcopy ${workspace}\\${SVN} ${workspace} /i /q /d /e & mkdir ${TARGET}" } 
        else
        { bat "xcopy ${workspace}\\${VERSION} ${workspace} /i /q /d /e & mkdir ${TARGET}" }
        break
        default: println ("Default: fm_host (armfm, pseutils, fmman); mm_win")
    }
}
