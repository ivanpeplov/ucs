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
                bat "7z a ${JOB_BASE_NAME}_${BUILD_NUMBER}_${SVN}_${VERSION}.zip *"
                bat "curl -s -u admin:${nexus_pwd} --upload-file ${JOB_BASE_NAME}_${BUILD_NUMBER}_${SVN}_${VERSION}.zip  ${NEXUS_URL}/${yy}/${mm}/${dd}/${JOB_BASE_NAME}/" 
                break
                case ['armfm']:
                bat "7z a ${ARCH}_${BUILD_NUMBER}.zip fmUX*, FmUX*"
                bat "curl -s -u admin:${nexus_pwd} --upload-file ${ARCH}_${BUILD_NUMBER}.zip  ${NEXUS_URL_1}/${yy}/${mm}/${dd}/${JOB_BASE_NAME}/" 
                break
                case ['ppcfm']:
                sh "zip -q ${ARCH}_${BUILD_NUMBER}.zip [f-F]mUX.*"
                sh "curl -s -u admin:"+'${nexus_pwd}'+" --upload-file ${ARCH}_${BUILD_NUMBER}.zip  ${NEXUS_URL_1}/${yy}/${mm}/${dd}/${JOB_BASE_NAME}/"
                break
                case ['fis', 'fis_util']:
                sh "zip -r -q ${JOB_BASE_NAME}_${BUILD_NUMBER}_${SVN}_${VERSION}.zip *"
                sh "curl  -s -u admin:"+'${nexus_pwd}'+" --upload-file ${JOB_BASE_NAME}_${BUILD_NUMBER}_${SVN}_${VERSION}.zip  ${NEXUS_URL}/${yy}/${mm}/${dd}/${JOB_BASE_NAME}/${NODE_NAME}/"
                break
                case ['micro_mod']:
                sh "zip -r -q ${JOB_BASE_NAME}_${ARCH}_${BUILD_NUMBER}.zip *"
                sh "curl  -s -u admin:"+'${nexus_pwd}'+" --upload-file ${JOB_BASE_NAME}_${ARCH}_${BUILD_NUMBER}.zip  ${NEXUS_URL}/${yy}/${mm}/${dd}/${JOB_BASE_NAME}/${NODE_NAME}/"
                break                  
                default:
                println "TBD"
            }   
        }
    }
}
