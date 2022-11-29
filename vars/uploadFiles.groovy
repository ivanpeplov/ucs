def call(String job, String path) {
    //path: ${TARGET}
    dir(path) {
        //Vars for folders naming (artifacts in nexus)
        def currentDate = new Date().format( 'yyyyMMdd' )
            echo currentDate
            //currentDate = sh (returnStdout: true, script: 'date -I').trim()
            dd = currentDate.substring(6)
            mm = currentDate.substring(4,6)
            yy = currentDate.substring(0,4)
        //file operations with artifacts
        def nexus_creds = [
            [path: 'secrets/creds/nexus', secretValues: [
                [envVar: 'nexus_pwd', vaultKey: 'password']]]]
            wrap([$class: 'VaultBuildWrapper', vaultSecrets: nexus_creds]) {
                //creating .zip artifact from bin/$TARGET folder and curl upload to nexus
            switch (job) {
                case ['tid_man', 'mms_eod', 'palmera']:
                loadWinBat('borlandUpload.bat')
                bat "borlandUpload.bat ${JOB_BASE_NAME} ${SVN} ${VERSION}"
               break
                case ['armfm']:
                    loadWinBat('armfmUpload.bat')
                    bat "armfmUpload.bat"
               break
                case ['ppcfm']:
                    loadLinuxScript('ppcfmUpload.sh')
                    sh "./ppcfmUpload.sh"
                break
                case ['fis', 'fis_util']:
                    loadLinuxScript('uploadFIS.sh')
                    sh "./uploadFIS.sh ${JOB_BASE_NAME} ${SVN} ${NODE_NAME} ${VERSION}"
                break
                case ['mm_nix']:
                    loadLinuxScript('uploadFIS.sh')
                    sh "./uploadFIS.sh ${JOB_BASE_NAME} ${ARCH} ${NODE_NAME}"
                break
                case ['mm_win']:
                    loadWinBat('mmUpload.bat')
                    bat "mmUpload.bat"
                break                  
                default:
                println "TBD"
            }   
        }
    }
}
