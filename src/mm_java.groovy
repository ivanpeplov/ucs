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
          proc= ["bash", "-c", "/var/lib/jenkins/bin/xidel.sh"].execute()
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
            //loadScript(place:'gradle', name:'tools.gradle')
            //loadScript(place:'gradle', name:'build.gradle')
            //sh "wget ${NEXUS_MAVEN_ORPO}/ru/ucscards/mmcore/${VERSION}/mmcore-${VERSION}.jar -O ./libs/mmcore.jar"
            sh "gradle -DARG=${VERSION} downloadFile -b tools_lib.gradle"
            sh "touch local.properties & echo 'sdk.dir = /home/jenkins/android' >> local.properties"
            sh "gradle build -b build_lib"
            sh "gradle publish -b tools_lib.gradle"
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