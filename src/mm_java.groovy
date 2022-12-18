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
          if (LABEL=='mmcore') {return["VT/MicroModuleJava/mmcore/trunk/mmcore"]}
          if (LABEL=='mmlibrary') {return["VT/MicroModuleJava/android/trunk/mmlibrary"]}
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
pipeline { //CI-69/CI-70
  agent {label 'JAVA'}
  environment {
    SVN_PATH = "${ROOT}" //full path for download fron SVN
    PATH="${PATH}:/home/jenkins/tools/gradle-6.1.1/bin:/home/jenkins/tools/apache-maven-3.8.6/bin"
    ANDROID_SDK_ROOT="/home/jenkins/android" //doesnt work. need a local.properties file
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
    stage('MMCORE') {
      when { expression  { LABEL == "mmcore" } }
      steps {
        dir ('mmcore') {
          script {
            //mmCoreGradle() //if you like a GRADLE
            loadScript(place:'gradle', name:'addToPom.xml')
            loadScript(place:'linux', name:'addToPom.sh')
            sh "./addToPom.sh; mvn deploy"
          }
        }
      }
    }
    stage('MMLIBRARY') {
      when { expression  { LABEL == "mmlibrary" } }
      steps {
        dir ('mmlibrary') {
          script {
            loadScript(place:'gradle', name:'tools_lib.gradle')
            loadScript(place:'gradle', name:'build_lib.gradle')
            loadScript(place:'linux', name:'deployMMlibrary.sh')
            sh "./deployMMlibrary.sh"
          }
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
        //sendEmail()               
      }//script
    }//failure
  }//post actions
}//pipeline