def call(String folder, String hsm) {
    node (NODE) {
    dir (folder) {
        switch (hsm) {
            case "gem":
            //obligatory for single makefile support
            //sh "sed -i 's;\\\\samples\\\\;/samples/;' Makefile"
            //temporary cp
                sh """
                cp ~/projects/cfgbuild.mak ${WORKSPACE}/FmUX
                sh "cp ~/projects/Makefile ${WORKSPACE}/FmUX/fm
                make
                """
            break
            default:
                bat "gnumake"
            }
        }
    }
}
