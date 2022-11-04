def call(String os) {
    node (NODE) {
    def dirpath="${TARGET}"
    dir(dirpath) {
        //condition for BASELIB/LIBFIS/MQLIB .a library copy action to bin/$TARGET folder
        if (params.LIB_UPLOAD) { sh "cp -R ${WORKSPACE}/lib/ ./lib" }
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
                switch (os) {
                    case ['tid_man', 'mms_eod', 'palmera']:
                    bat "7z a ${JOB_BASE_NAME}_${BUILD_NUMBER}_${SVN}_${VERSION}.zip *"
                    bat "curl -s -u admin:${nexus_pwd} --upload-file ${JOB_BASE_NAME}_${BUILD_NUMBER}_${SVN}_${VERSION}.zip  ${NEXUS_URL}/${yy}/${mm}/${dd}/${JOB_BASE_NAME}/" 
                    break
                    case 'eracom':
                    bat "7z a ${JOB_BASE_NAME}_${BUILD_NUMBER}.zip fmUX*, FmUX*"
                    bat "curl -s -u admin:${nexus_pwd} --upload-file ${JOB_BASE_NAME}_${BUILD_NUMBER}.zip  ${NEXUS_URL_1}/${yy}/${mm}/${dd}/${JOB_BASE_NAME}/" 
                    break
                    case 'gemalto':
                    sh "zip -q ${JOB_BASE_NAME}_${BUILD_NUMBER}.zip [f-F]mUX.*"
                    sh "curl -s -u admin:"+'${nexus_pwd}'+" --upload-file ${JOB_BASE_NAME}_${BUILD_NUMBER}.zip  ${NEXUS_URL_1}/${yy}/${mm}/${dd}/${JOB_BASE_NAME}/"
                    break
                    case 'fis':
                    sh "zip -r -q ${JOB_BASE_NAME}_${BUILD_NUMBER}_${SVN}_${VERSION}.zip *"
                    sh "curl  -s -u admin:"+'${nexus_pwd}'+" --upload-file ${JOB_BASE_NAME}_${BUILD_NUMBER}_${SVN}_${VERSION}.zip  ${NEXUS_URL}/${yy}/${mm}/${dd}/${JOB_BASE_NAME}/${NODE}/"
                    break                
                    default:
                    println "TBD"
                }   
            }
        }
    }
}
