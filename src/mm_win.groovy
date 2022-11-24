@Library("shared-library") _
pipeline { //CI-63
  agent {label 'borland'}
  environment {
    APP='MMW' //label for .yaml;
    TARGET = "${WORKSPACE}\\microx_t\\samples\\setup_p\\Release" //where find files for upload
    ROOT = "VT/MicroModule" //project root at SVN
    TOOR='MicroModule'
    SVN_PATH = "${ROOT}" //full path for download fron SVN
    PATH='C:\\Program Files\\Microsoft Visual Studio 12.0\\Common7\\IDE;C:\\Program Files\\GnuWin32\\bin;C:\\Program Files\\Borland\\CBuilder6\\Bin;C:\\Program Files\\MSBuild\\12.0\\Bin;C:\\Program Files\\Borland\\CBuilder6\\Projects\\Bpl;c:\\jenkins\\bin;c:\\Windows\\System32;C:\\Program Files\\TortoiseSVN\\bin;c:\\jenkins\\bin;C:\\Windows\\system32;C:\\Windows;C:\\Windows\\System32\\Wbem;C:\\Program Files\\Eclipse Adoptium\\jre-11.0.16.101-hotspot\\bin;C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\;C:\\Program Files\\Git\\bin,C:\\Program Files\\Git\\cmd,C:\\Program Files\\Git\\usr\\bin'
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
          prepareFiles('mm_win')      
        }
      }
    }
    stage('Build.. cyassl myizip microx') {
      steps {
        script {
          println 'Build.. cyassl myizip_z myizip_u microx'
          mmMSbuild("${WORKSPACE}")
        }
      }
    }
        stage('Build..  microp  ucs_xx  setup_p') {
      steps {
        script {
          println 'Build.. microp ucs_xx setup_p'
          mmMSbuild("microx_t/samples")
        }
      }
    }
    stage('UPLOAD') {
      steps {
        script {
          println 'UPLOAD'
          uploadFiles('mm_win', "${TARGET}")
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
          sendEmail()               
      }//script
    }//failure
  }//post actions
}//pipeline