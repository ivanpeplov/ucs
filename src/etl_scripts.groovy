@Library("shared-library") _
pipeline { //CI-60
    agent {label 'jenkins-rosa'}
    environment {
        ROOT="TestSQLtoNexus" //project root at SVN 
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
        stage('Extract Transform Load') {
            steps {
                script {
                    scriptPTH("TestSQLtoNexus")
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
