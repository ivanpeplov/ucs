//FM sign process
def call(String folder, String hsm) {
    dir (folder) {
        if (hsm=='ppcfm') {sh "mkfm -k ABG/fm -ffmUX.bin -oFmUX.fm <<< ${SIGN}"}
        else {bat "echo ${SIGN} | mkfm -k ABG/fm -ffmUX -ofmUX.fm"}
    }
}
