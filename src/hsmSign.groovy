//FM sign process
def call(String folder, String hsm) {
    node ("${NODE}") {
        dir (folder) {
            switch (hsm) {
            case "gem":
            sh "mkfm -k ABG/fm -ffmUX.bin -oFmUX.fm <<< ${GEM_SIGN}"
            break
            default:
            bat "echo ${ERA_SIGN} | mkfm -k ABG/fm -ffmUX -ofmUX.fm"
            break
            }
        }
    }
}
