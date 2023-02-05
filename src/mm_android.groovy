@Library("shared-library") _
properties([
  parameters([
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'Select Build:',
      name: 'LABEL', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: 
            'return["mmcore", "mmlibrary", "app", "evotor"]'
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
          if (LABEL=='mmcore') {return["VT/MicroModuleJava/mmcore/trunk/mmcore"]}
          if (LABEL=='mmlibrary') {return["VT/MicroModuleJava/android/trunk/mmlibrary"]}
          if (LABEL=='app') {return["VT/MicroModuleJava/android/trunk/app"]}
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
          if (LABEL=='app') {return["app/build/outputs/apk/debug"]}
          if (LABEL=='evotor') {return["evotor/app/build/outputs/apk/debug"]}
          '''
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT',
      description: "Select android minSDK parameter ", 
      referencedParameters: 'LABEL',
      name: 'MINSDK', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
          if (LABEL=='mmlibrary') {return["19", "23"]}
          '''
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT',
      description: 'Select mmCore version from REPO metadata',
      referencedParameters: 'LABEL',
      name: 'VERSION', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
          if (LABEL=='mmlibrary') {
          proc= ["bash", "-c", "/var/lib/jenkins/bin/getVersionMavenMetadata.sh"].execute()
          choices = proc.text.split().toList()
          return choices } 
          '''
        ]
      ]
    ]
  ])
])
pipeline { //CI-69/CI-70 - mmcore, mmlibrary;  CI-73/CI-74 - sample, evotor
  agent {label 'JAVA'}   
  environment {
    SVN_PATH = "${ROOT}" //full path for download fron SVN
    PATH="${PATH}:/home/jenkins/tools/gradle-6.1.1/bin:/home/jenkins/tools/apache-maven-3.8.6/bin"
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
      }
    }
    stage('BUILD') {
      steps {
        dir ("${LABEL}") {
          script {
            loadScript(place:'linux', name:'androidBuild.sh')
            if (LABEL=='mmcore') {loadScript(place:'gradle', name:'addToPom.xml')}
            sh "./androidBuild.sh"
          }
        }
      }
    }
    stage('UPLOAD') {
      when { expression  { LABEL == "app" || LABEL == "evotor"} }
      steps {
        uploadFiles('mm_android', "${TARGET}")
      }
    }
  }//stages
  post {
    always { cleanWs() }
    failure { script { sendEmail() } }
  }
}//pipeline