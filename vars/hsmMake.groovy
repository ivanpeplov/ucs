def call(String folder, String arch) {
    dir (folder) {
        switch (arch) {
            case "ppcfm": //gemalto
            //obligatory for single makefile support
            //sh "sed -i 's;\\\\samples\\\\;/samples/;' Makefile"
                loadLinuxScript('Makefile')
                loadLinuxScript('cfgbuild.mak')
                sh "cp ./cfgbuild.mak ${WORKSPACE}/FmUX/"
                if (TAIL=="") {sh "unset ${DEBUG} ; make"}
                else {sh "make debug=1"}             
            break
            default: //eracom
                if (TAIL=="") {bat "gnumake"}
                else {bat "gnumake debug=1"}             
        }
    }
}

