def call(String name) {
    def workspace = WORKSPACE.replaceAll('/','\\\\')
    switch (name) {//local preliminary file operations
        case ["ppcfm"] : //fm_host operations
            sh """ cp ./git/config/fmux/cfgbuild.mak ./FmUX
            cp ./git/config/fmux/Makefile ./FmUX/fm """
        break
        case ["borland"] : //borland operations
        if (SVN=='trunk' || SVN=='src') 
             { bat "xcopy ${workspace}\\${SVN} ${workspace} /i /q /d /e & mkdir ${TARGET}" } 
        else { bat "xcopy ${workspace}\\${VERSION} ${workspace} /i /q /d /e & mkdir ${TARGET}" }
        break
        default: println ("Default: armfm, pseutils, fmman, mm_win")
    }
}
