@Library("shared-library") _
properties([
  parameters([
      [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'Select Win 32/64',
      name: 'ARCH', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script:
          'return ["Win32", "x64"]'
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_CHECKBOX', 
      description: 'Select',
      referencedParameters: 'ARCH',
      name: 'REL', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
            switch(ARCH) {
            case ('x64') :
            return ["64:selected:disabled"]
            break
            default :
            return [" "]
            }
            '''
        ]
      ]
    ]
  ])
])
pipeline { //CI-63
  agent {label 'borland'}
  environment {
    APP='MMW' //label for .yaml; Borland CB pipelines
    TARGET = "${WORKSPACE}\\UPLOAD" //where find files for upload
    ROOT = "VT/MicroModule" //project root at SVN
    TOOR='MicroModule'
    SVN_PATH = "${ROOT}" //full path for download fron SVN
    PATH='C:\\Program Files\\Borland\\CBuilder6\\Bin;C:\\Program Files\\MSBuild\\12.0\\Bin;C:\\Program Files\\Borland\\CBuilder6\\Projects\\Bpl;c:\\jenkins\\bin;c:\\Windows\\System32;C:\\Program Files\\TortoiseSVN\\bin;c:\\jenkins\\bin;C:\\Windows\\system32;C:\\Windows;C:\\Windows\\System32\\Wbem;C:\\Program Files\\Eclipse Adoptium\\jre-11.0.16.101-hotspot\\bin;C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\;C:\\Program Files\\Git\\bin,C:\\Program Files\\Git\\cmd,C:\\Program Files\\Git\\usr\\bin'
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
          //getSVN()
          prepareFiles1('mm_win')      
        }
      }
    }
    stage('Build.. cyassl myizip microx') {
      steps {
        script {
          makeVC120("${WORKSPACE}", "${ARCH}", "${REL}")
        }
      }
    }
        stage('Build.. microp ucs_ms ucs_dt ucs_mm') {
      steps {
        script {
          println 'BUILD MICROP UCS_xx'
          makeVC120("${WORKSPACE}/microx_t/samples", "${ARCH}", "${REL}")
        }
      }
    }
        stage('Build setup_p') {
      steps {
        script {
          println 'BUILD SETUP_P'
          //makeVC120("${WORKSPACE}", "${ARCH}", "${REL}")
        }
      }
    }
    stage('UPLOAD') {
      steps {
        script {
          println 'UPLOAD'
          //uploadFiles('palmera', "${TARGET}")
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