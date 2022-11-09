//FM sign process
def call(String folder, String hsm) {
    dir (folder) {
        switch (hsm) {
        case  "ppcfm": //gemalto
        sh "mkfm -k ABG/fm -ffmUX.bin -oFmUX.fm <<< ${SIGN}"
        break
        default: //eracom
        bat "echo ${SIGN} | mkfm -k ABG/fm -ffmUX -ofmUX.fm"
        break
        }
    }
}
