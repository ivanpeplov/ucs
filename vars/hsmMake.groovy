def call(String folder, String hsm) {
    node (NODE) {
    dir (folder) {
        switch (hsm) {
            case "gemalto":
            //obligatory for single makefile support
            //sh "sed -i 's;\\\\samples\\\\;/samples/;' Makefile"
            //temporary cp
                sh """
                cp ~/projects/cfgbuild.mak ${WORKSPACE}/FmUX/
                cp ~/projects/Makefile ${WORKSPACE}/FmUX/fm/
                """
                if (DIR=='') {sh "unset ${DEBUG}; make"}
                else {sh "make debug=1"}
                
            break
            default:
                if (DIR=='') {bat "gnumake"}
                else {bat "gnumake debug=1"}
            }
        }
    }
}
