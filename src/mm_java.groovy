@Library("shared-library") _
properties([
  parameters([
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'Select:  MM_CORE,  MM_LIB',
      name: 'LABEL', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: 
            'return["mmcore", "mmlibrary"]'
        ]
      ]
    ],
      [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      referencedParameters: 'LABEL',
      name: 'ROOT', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
          if (LABEL=='mmcore') {return["VT/MicroModuleJava/mmcore/trunk"]}
          if (LABEL=='mmlibrary') {return["VT/MicroModuleJava/android/trunk/mmlibrary"]}
          '''
        ]
      ]
    ]
  ])
])
pipeline { //CI-69/CI-70
  agent {label 'JAVA'}
  environment {
    SVN_PATH = "${ROOT}" //full path for download fron SVN
    PATH="${PATH}:/home/jenkins/tools/gradle-6.1.1/bin"
    ANDROID_SDK_ROOT="/home/jenkins/android"
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
          prepareFiles("${JOB_BASE_NAME}")      
        }
      }
    }
    stage('MMCORE') {
      when { expression  { LABEL == "mmcore" } }
      steps {
        dir ('mmcore') {
          script {
            println "mmcore"
            sh "gradle build --info & gradle  publish --info"
          }
        }
      }
    }
    stage('MMLIBRARY') {
      when { expression  { LABEL == "mmlibrary" } }
      steps {
        dir ('mmlibrary') {
          script {
            println "mmlib"
            sh "gradle downloadFile --info & gradle build --info & gradle  publish --info"
          }
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