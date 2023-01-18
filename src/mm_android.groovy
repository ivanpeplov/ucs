@Library("shared-library") _
properties([
  parameters([
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'Select:  SAMPLE,  EVOTOR',
      name: 'LABEL', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: 
            'return["sample", "evotor"]'
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
          if (LABEL=='sample') {return["VT/MicroModuleJava/android/trunk/app"]}
          if (LABEL=='evotor') {return["VT/MicroModuleJava/evotor"]}
          '''
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      referencedParameters: 'LABEL',
      name: 'TARGET', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
          if (LABEL=='sample') {return["app/build/outputs/apk/debug"]}
          if (LABEL=='evotor') {return["evotor/app/build/outputs/apk/debug"]}
          '''
        ]
      ]
    ]
  ])
])
pipeline { //CI-73/CI-74
  agent {label 'JAVA'}   
  environment {
    SVN_PATH = "${ROOT}" //full path for download fron SVN
    PATH="${PATH}:/home/jenkins/tools/gradle-6.1.1/bin:/home/jenkins/tools/apache-maven-3.8.6/bin"
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
          prepareFiles("${LABEL}")
        }
      }
    }
    stage('SAMPLE') {
      when { expression  { LABEL == "sample" } }
      steps {
        dir ('app') {
          script {
            loadScript(place:'linux', name:'deployMMlibrary.sh')
            loadScript(place:'gradle', name:'sample.gradle')
            sh "./deployMMlibrary.sh"
          }
        }
      }
    }
    stage('EVOTOR') {
      when { expression  { LABEL == "evotor" } }
      steps {
        dir ('evotor') {
          script {
            loadScript(place:'linux', name:'deployMMlibrary.sh')
            loadScript(place:'gradle', name:'evotor.gradle')
            loadScript(place:'gradle', name:'evotor_app.gradle')
            sh "./deployMMlibrary.sh"
          }
        }
      }
    }
    stage('UPLOAD') {
      steps {
        script {
          uploadFiles('mm_android', "${TARGET}")
        }
      }
    }
  }//stages
  post {
    always { cleanWs() }
    failure { script { sendEmail() } }
  }//post
}//pipeline