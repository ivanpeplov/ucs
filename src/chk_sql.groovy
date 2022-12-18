@Library("shared-library") _
properties([
  parameters([
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'Select TIETO release',
      name: 'MNR', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
            svn_url='172.16.10.230/scm/svn/dev'
            proj='TestSQLtoNexus'
            proc1= ["bash", "-c", "svn list --username jenkins --password mRovmZVpt  https://${svn_url}/${proj}"].execute()
            proc2= ["bash", "-c", "rev | cut -c2- | rev"].execute()
            all = proc1 | proc2
            choices = all.text
            return choices.split().toList()
            '''
        ]
      ]
    ],
  ])
])
pipeline { //CI-60/CI-67
    agent {label 'jenkins-rosa'}
    environment {
        ROOT="TestSQLtoNexus/${MNR}" //project root at SVN 
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
                    println "ETL cycle"
                    pthScript("${MNR}")
                }
            }
        }
    } //stages
    post {
        always {
            echo 'Clean Workspace'
            cleanWs()
        }//always
        failure {
            script {
                echo 'emailing'
                sendEmail()               
            }//script
        }//failure
    } //post actions
} //pipeline
