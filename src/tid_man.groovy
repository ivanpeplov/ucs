@Library("shared-library") _
properties([
  parameters([
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'Select Trunk, Branches or Tags',
      filterLength: 1,
      filterable: false,
      name: 'SVN', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: 
            'return["trunk", "branches", "tags"]'
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'Select Version for Tags/Branches',
      filterLength: 1,
      filterable: false,
      referencedParameters: 'SVN',
      name: 'VERSION', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
            svn_ip='172.16.10.230'
            svn_url='scm/svn/dev'
            proj_root='CardPro/TidManager/TID_v6'
            if (SVN == "tags" || SVN == "branches") {
            proc1= ["bash", "-c", "svn list --username jenkins --password mRovmZVpt  https://${svn_ip}/${svn_url}/${proj_root}/${SVN}"].execute()
            proc2= ["bash", "-c", "rev | cut -c2- | rev"].execute()
            all = proc1 | proc2
            choices = all.text
            return choices.split().toList()
            }
            '''
        ]
      ]
    ]
  ])
])
pipeline {
  agent {label 'BORLAND'}
  environment {
    APP='TID'
    TARGET = "${WORKSPACE}\\UPLOAD" //target folder for binaries
    NODE = 'BORLAND'
    PROJ_ROOT = "CardPro/TidManager/TID_v6"
    SVN_PATH = "${PROJ_ROOT}/${SVN}/${VERSION}"
    PATH='C:\\Program Files\\Borland\\CBuilder6\\Bin;C:\\Program Files\\Borland\\CBuilder6\\Projects\\Bpl;c:\\jenkins\\bin;c:\\Windows\\System32;C:\\Program Files\\TortoiseSVN\\bin;c:\\jenkins\\bin;C:\\Windows\\system32;C:\\Windows;C:\\Windows\\System32\\Wbem;C:\\Program Files\\Eclipse Adoptium\\jre-11.0.16.101-hotspot\\bin;C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\;C:\\Program Files\\Git\\bin,C:\\Program Files\\Git\\cmd,C:\\Program Files\\Git\\usr\\bin'
  }
stages {
    stage('SET ENV') {
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
            prepareFiles('tid_man')      
          }
        }
    }
    stage('CARDLIB') {
        steps {
            script {
                makeBorland('CARDLIB')
            }
        }
    }
    stage('FORM') {
      steps {
        script {
            makeBorland('FORM')
        }
      }
    }
    stage('CARDPRO.EXE') {
      steps {
        script {
            makeBorland('C:\\jenkins\\workspace\\UCS\\tid_man')
            makeBorland('C:\\Windows\\System32')
            makeBorland('FORM\\PRINT.CFG')
        }
      }
    }
    stage('Upload') {
      steps {
        script {
            uploadFiles('tid_man')
        }
      }
    }
  }//stages
  post {
      always {
          script {             
              echo 'Clean Workspace'
              cleanWs()
          }//script
      }//always
      failure {
          script {
              //emailing
              echo 'email'
              //sendEmail()               
          }//script
      }//failure
  }//post actions
}//pipeline