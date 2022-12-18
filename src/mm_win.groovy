@Library("shared-library") _
pipeline { //CI-63
  agent {label 'borland'}
  environment {
    APP='MMW' //label for .yaml;
    TARGET = "${WORKSPACE}\\Micromodule\\microx_t\\samples\\rel_p" //where find files for upload
    ROOT = "VT/MicroModule" //project root at SVN
    TOOR='MicroModule/Windows' // upload point at Nexus
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
          getSVN()    
        }
      }
    }
    stage('Build.. cyassl myizip microx') {
      steps {
        dir ("Micromodule") {
          script {
            loadScript(place:'win', name:'mmBuild.bat')
            mmWin(mm, arch) // mm, arch - strings from environment.yml file
          }
        }
      }
    }
    stage('Build..  microp  ucs_xx  setup_p') {
      steps {
        dir ("Micromodule/microx_t/samples") {
          script {
            loadScript(place:'win', name:'mmBuild.bat')
            mmWin(mmm, arch) //strings from environment.yml file
            loadScript(place:'win', name:'mmArt.bat')
            bat (script:"mmArt.bat") // mmm, arch - build for setup_p.zip from setup_p.msi
          }
        }
      }
    }
    stage('UPLOAD') {
      steps {
        script {
          uploadFiles('mm_win', "${TARGET}")
        }
      }
    }
  }//stages
  post {
    always {
      echo 'Clean Workspace'
      cleanWs()
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