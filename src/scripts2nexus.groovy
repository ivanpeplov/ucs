@Library("shared-library") _
pipeline { //CI-57
    agent {label 'jenkins-rosa'}
    environment {
        ROOT="NexusShareAsIs" //project root at SVN
        SVN_PATH ="${ROOT}" //full path for download fron SVN
    }
    stages {
        stage('SET Env') {
            steps {
                script {
                    setDescription()
                    setEnv()
                }
            }
        }
        stage ('PREPARE') {
            steps {
                script {
                    getSVN()
                }
            }
        }
        stage('GROUP.. UPLOAD') {
            steps {
                dir(ROOT) {
                    script {
                    def nexus_creds = [
                    [path: 'secrets/creds/nexus', secretValues: [
                    [envVar: 'nexus_pwd', vaultKey: 'password']]]]
                    sh "find . -type d -name .svn -exec rm -rf {} +" //to delete ./svn folder recursively
                    loadScript(place:'linux', name:'ktrUpload.sh') //bash for curl to Nexus
                    lvl1 = listDir("${ROOT}")
                        for (xo in lvl1) { lvl2=listDir("${ROOT}/${xo}")
                            for (yo in lvl2) { lvl3=listDir("${ROOT}/${xo}/${yo}")
                                for (zo in lvl3) {
                                wrap([$class: 'VaultBuildWrapper', vaultSecrets: nexus_creds]) {
                                sh "./ktrUpload.sh ${xo} ${yo} ${zo}" } } } }
                    }
                }
            }
        }
    } //stages
    post {
        always {
            script {             
                echo 'Clean Workspace'
                cleanWs()
            }//script
        }//always
        failure {
            script {
                echo 'emailing'
                sendEmail()               
            }//script
        }//failure
    } //post actions
} //pipeline