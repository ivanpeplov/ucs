def call(String folder, String arch) {
    dir (folder) {
        switch (arch) {
            case "ppcfm": //gemalto
            //obligatory for single makefile support
            //sh "sed -i 's;\\\\samples\\\\;/samples/;' Makefile"
            //temporary cp
                sh """
                cp ${WORKSPACE}/git/config/fm/cfgbuild.mak ${WORKSPACE}/FmUX/
                cp ${WORKSPACE}/git/config/fm/Makefile ${WORKSPACE}/FmUX/fm/
                """
                if (TAIL=='') {sh "unset ${DEBUG}; make"}
                else {sh "make debug=1"}              
            break
            default: //eracom
                if (TAIL=='') {bat "gnumake"}
                else {bat "gnumake debug=1"}
        }
    }
}

