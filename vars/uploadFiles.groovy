def call(String job, String path) {
    //path: ${TARGET}
    dir(path) {
        def currentDate = new Date().format( 'yyyyMMdd' )
            echo currentDate
            //currentDate = sh (returnStdout: true, script: 'date -I').trim()
            dd = currentDate.substring(6)
            mm = currentDate.substring(4,6)
            yy = currentDate.substring(0,4)
        def nexus_creds = [
            [path: 'secrets/creds/nexus', secretValues: [
                [envVar: 'nexus_pwd', vaultKey: 'password']]]]
            wrap([$class: 'VaultBuildWrapper', vaultSecrets: nexus_creds]) {
            switch (job) {
                case ['borland']:
                    loadScript(place:'win', name:'borlandUpload.bat')
                    if (LABEL=='passkey') {SVN='trunk'}
                    bat "borlandUpload.bat ${LABEL} ${SVN} ${VERSION}"
                break
                case ['armfm']:
                    loadScript(place:'win', name:'fmUpload.bat')
                    bat "fmUpload.bat"
                break
                case ['ppcfm']:
                    loadScript(place:'linux', name:'fmUpload.sh')
                    sh "./fmUpload.sh"
                break
                case ['fis']:
                    loadScript(place:'linux', name:'mmUpload.sh')
                    sh "./mmUpload.sh ${LABEL} ${SVN} ${NODE_NAME} ${VERSION}"
                break
                case ['mm_android']:
                loadScript(place:'linux', name:'androidUpload.sh')
                sh "./androidUpload.sh ${LABEL}"
                break
                case ['mm_nix']:
                    loadScript(place:'linux', name:'mmUpload.sh')
                    sh "./mmUpload.sh ucs_mm ${ARCH} ${NODE_NAME}"
                break
                case ['mm_win']:
                    loadScript(place:'win', name:'mmUpload.bat')
                    bat "mmUpload.bat"
                break                  
            }   
        }
    }
}
