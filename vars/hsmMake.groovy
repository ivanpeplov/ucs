def call(String folder, String hsm) {
    dir (folder) {
        switch (hsm) {
            case "gemalto":
            //obligatory for single makefile support
            //sh "sed -i 's;\\\\samples\\\\;/samples/;' Makefile"
            //temporary cp
                sh """
                cp ${WORKSPACE}/git/config/cfgbuild.mak ${WORKSPACE}/FmUX/
                cp ${WORKSPACE}/git/config/Makefile ${WORKSPACE}/FmUX/fm/
                """
                if (TAIL=='') {sh "unset ${DEBUG}; make"}
                else {sh "make debug=1"}              
            break
            default:
                if (TAIL=='') {bat "gnumake"}
                else {bat "gnumake debug=1"}
        }
    }
}

