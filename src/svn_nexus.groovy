@Library("shared-library") _
pipeline {
    agent {label 'ROSA'}
    options {
        timeout(time: 10, unit: 'MINUTES') 
    }
    environment {
    NODE='ROSA'
    SVN_PATH = "NexusShareAsIs"
    ROOT="${WORKSPACE}/${SVN_PATH}"
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
        stage('CONVERTING') {
            steps {
                script {
                    conversionNexus()
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