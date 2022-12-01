@Library("shared-library") _
properties([
  parameters([
      [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'Select node to run',
      name: 'NODE_NAME', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
          label='MICROMOD'
          def nodes = jenkins.model.Jenkins.get().computers
          .findAll{ it.node.labelString.contains(label) }
          .collect{ it.node.selfLabel.name }
          return nodes
          '''
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_CHECKBOX', 
      referencedParameters: 'NODE_NAME',
      name: 'ARCH', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
          if (NODE_NAME=='jenkins-ubuntu') {return ["x64:selected:disabled"]}
          if (NODE_NAME=='jenkins-rosa') {return ["x64:selected:disabled"]}
          if (NODE_NAME=='jenkins-fedora') {return ["x64:selected:disabled"]}
          '''
        ]
      ]
    ]
  ])
])
pipeline { //CI-62
    agent {label NODE_NAME}
    options { timeout(time: 10, unit: 'MINUTES') }
    environment {
      APP='MMX' //label for .yaml;
      ARCH='x64' //temp
      TARGET='units/microx_t/samples/Linux_Install'
      //TARGET='bin' //where find files for upload
      ROOT='VT/MicroModule' //project root at SVN
      TOOR='MicroModule/Linux' // upload trunk at Nexus
      SVN_PATH = "${ROOT}" //full path for download fron SVN
      //environment for build
      PROJECTS="/home/jenkins/workspace/${JOB_NAME}" //Not use ${WORKSPACE} here
      INCLUDE="-I. -I${PROJECTS}/units -I./include -I../include"
      LIB='-L${PROJECTS}/lib'
    }
    stages {
      stage('SET Env') {
        steps {
          script {
            //catchErrors()
            setDescription()
            setEnv()
          }
        }
      }
      stage ('PREPARE') {
        steps {
          script {
            getSVN()
            prepareFiles('mm_nix')
          }
        }
      }
      stage(' CYASSL MYIZIP_Z MICROX_T') {
        steps {
          script {
            mmMake('units', "${ARCH}")
          }
        }
      }
      stage('MICROP UCS_MS UCS_DT UCS_MM') {
        steps {
          script {
            mmMake('units/microx_t/samples', "${ARCH}")
          }
        }
      }
      stage('UPLOAD') {
        steps {
          script {
            uploadFiles('mm_nix', "${TARGET}")
          }
        }
      }
    } //stages
    post {
      always {
        script {             
            echo "Clean Workspace"
            cleanWs()
        }//script
      }//always
      failure {
        script {
          //emailing
          sendEmail()               
        }//script
      }//failure
    } //post actions
} //pipeline