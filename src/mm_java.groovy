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
          proc= ["bash", "-c", "/var/lib/jenkins/xidel.sh"].execute()
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
            loadScript(place:'gradle', name:'build_core.gradle')
            loadScript(place:'linux', name:'getVersionFromSvnPom.sh')
            output = sh returnStdout: true, script: "./getVersionFromSvnPom.sh"
            versionFromPom = output.trim()
            sh "gradle -Pversion=${versionFromPom} build"
            sh "gradle -Pversion=${versionFromPom} publish"
          }
        }
      }
    }
    stage('MMLIBRARY') {
      when { expression  { LABEL == "mmlibrary" } }
      steps {
        dir ('mmlibrary') {
          script {
            loadScript(place:'gradle', name:'build_lib.gradle')
            println "-------------------DownloadFile----------------------------"
            //sh "wget ${NEXUS_MAVEN}/ru/ucs/mmcore/${VERSION}/mmcore-${VERSION}.jar -O ./libs/mmcore.jar"
            sh "gradle -DARG=${VERSION} downloadFile"
            println "----------------------BUILD--------------------------------" 
            sh "gradle build"
            println "---------------------PUBLISH--------------------------------"
            sh "gradle publish "
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