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
                if ("${VERSION}"=='')
                    {bat "7z a ${JOB_BASE_NAME}_${SVN}.zip *"
                    bat "curl -u jenkucs_sa:${nexus_pwd} --upload-file ${JOB_BASE_NAME}_${SVN}.zip  ${NEXUS_URL}/Borland/" }
                    else 
                    {bat "7z a ${JOB_BASE_NAME}_${SVN}_${VERSION}.zip *"
                    bat "curl -u jenkucs_sa:${nexus_pwd} --upload-file ${JOB_BASE_NAME}_${SVN}_${VERSION}.zip  ${NEXUS_URL}/Borland/"}
                break
                case ['armfm']:
                    bat "7z a ${ARCH}_${BUILD_NUMBER}.zip fmUX*, FmUX*"
                    bat "curl  -u jenkucs_sa:${nexus_pwd} --upload-file ${ARCH}_${BUILD_NUMBER}.zip  ${NEXUS_URL_1}/FM/" 
                break
                case ['ppcfm']:
                    sh "zip -q ${ARCH}_${BUILD_NUMBER}.zip [f-F]mUX.*"
                    sh "curl -s -u jenkucs_sa:"+'${nexus_pwd}'+" --upload-file ${ARCH}_${BUILD_NUMBER}.zip  ${NEXUS_URL_1}/FM/"
                break
                case ['fis', 'fis_util']:
                    loadLinuxScript("uploadFIS.sh")
                    {sh( "./uploadFIS.sh ${JOB_BASE_NAME} ${SVN} ${NODE_NAME} ${VERSION}")}
                break
                case ['mm_nix']:
                    loadLinuxScript("uploadFIS.sh")
                    {sh( "./uploadFIS.sh ${JOB_BASE_NAME} ${ARCH} ${NODE_NAME}")}
                break
                case ['mm_win']:
                    bat "curl  -s -u jenkucs_sa:${nexus_pwd} --upload-file setup_p.zip  ${NEXUS_URL}/${TOOR}"
                break                  
                default:
                println "TBD"
            }   
        }
    }
}
