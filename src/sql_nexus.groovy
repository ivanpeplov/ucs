@Library("shared-library") _
pipeline {
    agent {label 'jenkins-rosa'}
    options { timeout(time: 10, unit: 'MINUTES') }
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
                    echo 'no svn'
                    //getSVN()
                    prepareFiles_sql('sql_nexus')
                }
            }
        }
        stage('GROUP.. UPLOAD') {
            steps {
                script {
                    pentahoNexus("TestSQLtoNexus")
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
