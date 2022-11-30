def call(String folder, String arch) {
    dir (folder) {
        switch (arch) {
            case "ppcfm": //gemalto
            //obligatory for single makefile support
            //sh "sed -i 's;\\\\samples\\\\;/samples/;' Makefile"
            loadScript(place:'fmux', name:'Makefile')
            loadScript(place:'fmux', name:'cfgbuild.mak')
            sh "cp ./cfgbuild.mak ${WORKSPACE}/FmUX/"
            TAIL=="" ? sh(script:"unset ${DEBUG} ; make") : sh(script:"make debug=1") //elvis operator   
            break
            default: //eracom
            TAIL=="" ? bat(script:"gnumake") : bat(script:"gnumake debug=1") //elvis operator          
        }
    }
}

