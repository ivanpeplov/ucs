@Library("shared-library") _
pipeline { //CI-57
    agent {label 'jenkins-rosa'}
    options { timeout(time: 10, unit: 'MINUTES') }
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
                script {
                    ktrToNexus("NexusShareAsIs")
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