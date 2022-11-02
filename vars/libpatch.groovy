//Reading from environment.yaml
def set_env() {
    def yaml_cfg = readYaml file: "${WORKSPACE}/config/hosts.yaml"
    env.MAIL_RECIPIENTS_TS = yaml_cfg.get('mail_recipients_ts')
    servers = yaml_cfg.server
}
def prepare() {
    node ( "OW" ) {
        // checkout our git
        checkout changelog: false, poll: false, scm: [
            $class: 'GitSCM', 
            branches: [[name: '*/main']], 
            extensions: [], 
            userRemoteConfigs: [[credentialsId: 'jenkins_gitlab_token', url: 'http://10.255.252.160/prime/js.git']]]
        def vm_creds = [
            [path: 'secrets/creds/vsphere', secretValues: [
                [envVar: 'j_pwd', vaultKey: 'password']]]
        ]   
            wrap([$class: 'VaultBuildWrapper', vaultSecrets: vm_creds]) {
                SESSION_ID_TMP = sh (script: """curl -s -k -X POST https://vcenter.internal/rest/com/vmware/cis/session -u  'jenkucs_sa':'${j_pwd}'""", returnStdout: true)
            }
            SESSION_ID = SESSION_ID_TMP.drop(10).take(32)
        }
}
def power(String action, String reg) {
    node ( "OW" ) {
        switch (action) {
            case ('shutdown'):
                for (server in tools.getVmForRegion(region: "${reg}").split(' ')) {
                //Using REST API call for immediate shutdown
                sh """
                #curl -s -k -X POST -H "vmware-api-session-id: ${SESSION_ID}" "https://vcenter.internal/api/vcenter/vm/{${server}}/guest/power?action=${action}"
                curl -s -k -X GET -H "vmware-api-session-id: ${SESSION_ID}" "https://vcenter.internal/rest/vcenter/vm/{${server}}/guest/identity"
                """ }
                //Using Vsphere Cloud plugin for shutdown
                //for (server in tools.getNameForRegion(region: "${reg}").split(' ')) {
                //vSphere buildStep: [$class: 'PowerOff', evenIfSuspended: false, ignoreIfNotExists: false, shutdownGracefully: true, vm: server ], serverName: 'vcenter' }
            break
            default:
                for (server in tools.getVmForRegion(region: "${reg}").split(' ')) {
                sh """
                #curl -s -k -X POST -H "vmware-api-session-id: ${SESSION_ID}" "https://vcenter.internal/rest/vcenter/vm/{${server}}/power/${action}"
                curl -s -k -X GET -H "vmware-api-session-id: ${SESSION_ID}" "https://vcenter.internal/rest/vcenter/vm/{${server}}/guest/identity"
                """ }
            break
        }
    }
}

def gates(String action, String reg) {
    for (server in tools.getVmForRegion(region: "${reg}").split(' ')) {
        node ( "OW" ) {
            sh """
            #curl -s -k -X POST -H "vmware-api-session-id: ${SESSION_ID}" "https://vcenter.internal/rest/vcenter/vm/{${server}}/hardware/ethernet/{4000}/${action}"
            curl -s -k -X GET -H "vmware-api-session-id: ${SESSION_ID}" "https://vcenter.internal/rest/vcenter/vm/{${server}}/hardware/ethernet"
            """
        }
    }
}
def rdp(String reg) {
    for (server in tools.getNameForRegion(region: "${reg}").split(' ')) {
        node ( "OW" ) {
            server_user='jenkucs_sa'
            sh """
            #scp ./win/rdpsessions.ps1 ${server_user}@${server}:/home/${server_user}/rdpsessions.ps1
            #ssh ${server_user}@${server} "chmod u+x /home/${server_user}/rdpsessions.ps1"
            ssh ${server_user}@${server} "powershell "./\\\\rdpsessions.ps1 ${server}""
            """
        }
    }
}


//E-mailing Build Report
def send_email () {
    startTime = new Date(currentBuild.startTimeInMillis).format('dd.MM.yyyy HH:mm')
    emailext attachLog: true, 
    compressLog: true,
    body: """
        Build Start Date&Time: ${startTime}
        Build Status: ${currentBuild.result}
        Launched by ${BUILD_USER_ID}
        ${JOB_BASE_NAME}-${BUILD_NUMBER}""",
            from: 'prime_news@ucscards.ru',
            subject: "Build Status ${currentBuild.result}, ${JOB_BASE_NAME}-${BUILD_NUMBER}",
            to: "${env.MAIL_RECIPIENTS_DEV}";
}
return this