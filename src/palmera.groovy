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
            svn_url='172.16.10.230/scm/svn/dev'
            proj_root='Util/PalmeraLoader'
            if (SVN == "tags" || SVN == "branches") {
            proc1= ["bash", "-c", "svn list --username jenkins --password mRovmZVpt  https://${svn_url}/${proj_root}/${SVN}"].execute()
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
  agent {label 'borland'}
  environment {
    APP='PALMERA' //label for .yaml; Borland CB pipelines
    TARGET = "${WORKSPACE}\\UPLOAD" //where find files for upload
    ROOT = "Util/PalmeraLoader" //project root at SVN
    SVN_PATH = "${ROOT}/${SVN}/${VERSION}" //full path for download fron SVN
    PATH='C:\\Program Files\\Borland\\CBuilder6\\Bin;C:\\Program Files\\Borland\\CBuilder6\\Projects\\Bpl;c:\\jenkins\\bin;c:\\Windows\\System32;C:\\Program Files\\TortoiseSVN\\bin;c:\\jenkins\\bin;C:\\Windows\\system32;C:\\Windows;C:\\Windows\\System32\\Wbem;C:\\Program Files\\Eclipse Adoptium\\jre-11.0.16.101-hotspot\\bin;C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\;C:\\Program Files\\Git\\bin,C:\\Program Files\\Git\\cmd,C:\\Program Files\\Git\\usr\\bin'
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
          prepareFiles('palmera')      
        }
      }
    }
    stage('BUILD') {
      steps {
        script {
          makeBorland('C:\\jenkins\\workspace\\UCS\\palmera')
        }
      }
    }
    stage('UPLOAD') {
      steps {
        script {
          uploadFiles('palmera', "${TARGET}")
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