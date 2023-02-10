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
      choiceType: 'PT_SINGLE_SELECT',
      description: 'Selected 32/64 bit', 
      referencedParameters: 'NODE_NAME',
      name: 'OS_ARCH', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
          if (NODE_NAME=='jenkins-ubuntu-32') {return ["32"]}
          else {return ["64"]}
          '''
        ]
      ]
    ]
  ])
])
pipeline { //CI-62
  agent {label NODE_NAME}
  environment {
    APP='MMX' //label for .yaml;
    TARGET='units/microx_t/samples/Linux_Install'
    ROOT='VT/MicroModule' //project root at SVN
    TOOR='MicroModule/Linux' // upload point at Nexus
    SVN_PATH = "${ROOT}" //full path for download fron SVN
    PROJECTS="/home/jenkins/workspace/${JOB_NAME}" //Not use ${WORKSPACE} here
    PATH="${PATH}:${PROJECTS}/tools:${PROJECTS}/units:${PROJECTS}/bin"
    INCLUDE="-I. -I${PROJECTS}/units -I./include -I../include"
    LIB='-L${PROJECTS}/lib'
  }
  stages {
    stage('SET Env') {
      steps {
        setDescription()
        setEnv()
      }
    }
    stage ('PREPARE') {
      steps {
        getSVN()
        prepareFiles("mm_nix")
      }
    }
    stage('BUILD') {
      steps {
        dir ('units') {
          script {
            mmBuild.Nix(mm) // mm - string CYASSL,MYIZIP_Z,MICROX_T from environment.yml file
          }
        }
        dir ('units/microx_t/samples') {
          script {
            mmBuild.Nix(mmm) // mmm - string MICROP,UCS_XX from environment.yml file
            loadScript(place:'linux', name:'mmArt.sh')
            sh "./mmArt.sh" // prepare for upload
          }
        }
      }
    }
    stage('UPLOAD') {
      steps { 
        uploadFiles('mm_nix', "${TARGET}")
      }
    }
  } //stages
  post {
    always { cleanWs() }
    failure { sendEmail() }
  }
} //pipeline