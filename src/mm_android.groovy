@Library("shared-library") _
properties([
  parameters([
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'Select:',
      name: 'LABEL', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: 
            'return["mmcore", "mmlibrary", "sample", "evotor"]'
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
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT',
      description: "Select minimal SDK version for MMlibrary build", 
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
          prepareFiles("${LABEL}")
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
            loadScript(place:'linux', name:'deployMMlibrary.sh')
            sh "./androidBuild.sh"
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
            sh "./androidBuild.sh"
          }
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
            sh "./androidBuild.sh"
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
            sh "./androidBuild.sh"
          }
        }
      }
    }
    stage('UPLOAD') {
      when { expression  { LABEL == "sample" || LABEL == "evotor"} }
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