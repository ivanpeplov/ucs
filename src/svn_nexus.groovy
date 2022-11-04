@Library("shared-library") _
pipeline {
    agent {label 'ROSA'}
    options {
        timeout(time: 10, unit: 'MINUTES') 
    }
    environment {
    NODE='ROSA'
    ROOT="NexusShareAsIs"
    SVN=' '
    VERSION=' '
    SVN_PATH ="NexusShareAsIs"
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
                    uploadNexus("NexusShareAsIs")
                }
            }
        }
    } //stages
        post {
            always {
                script {             
                    echo 'Clean Workspace'
                    cleanWs()
                    clearSlave()
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