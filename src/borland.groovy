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
            'return["cardpro", "mmseod", "palmerauloade", "passkey"]'
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
          if (LABEL=='mmseod') {return["MMS/mmsEOD"]}
          if (LABEL=='palmerauloade') {return["Util/PalmeraLoader"]}
          if (LABEL=='passkey') {return["PassKey/PassKey"]}
          if (LABEL=='cardpro') {return["CardPro/TidManager/TID_v6"]}
          '''
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      referencedParameters: 'LABEL',
      name: 'APP', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
          if (LABEL=='mmseod') {return["MMS"]}
          if (LABEL=='palmerauloade') {return["PALMERA"]}
          if (LABEL=='passkey') {return["PASSKEY"]}
          if (LABEL=='cardpro') {return["TID"]}
          '''
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'Select Trunk, Branches or Tags',
      referencedParameters: 'LABEL',
      name: 'SVN', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
          if (LABEL=='passkey') { return ["src"] }
          else { return ["trunk", "branches", "tags"] } '''
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'Select Version for Tags/Branches',
      referencedParameters: 'SVN, ROOT',
      name: 'VERSION', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
            svn_url='172.16.10.230/scm/svn/dev'
            if (SVN == "tags" || SVN == "branches") {
            proc1= ["bash", "-c", "svn list --username jenkins --password mRovmZVpt  https://${svn_url}/${ROOT}/${SVN}"].execute()
            proc2= ["bash", "-c", "rev | cut -c2- | rev"].execute()
            all = proc1 | proc2
            choices = all.text
            return choices.split().toList()
            }
            '''
        ]
      ]
    ]
  ])
])
pipeline { //CI-56,58,59,72
  agent {label 'borland'}
  environment {
    TARGET = "${WORKSPACE}\\UPLOAD" //target folder for binaries
    SVN_PATH = "${ROOT}/${SVN}/${VERSION}" //full path for download fron SVN
    PATH='C:\\Program Files\\Borland\\CBuilder6\\Bin;C:\\Program Files\\Borland\\CBuilder6\\Projects\\Bpl;c:\\jenkins\\bin;c:\\Windows\\System32;C:\\Program Files\\TortoiseSVN\\bin;c:\\jenkins\\bin;C:\\Windows\\system32;C:\\Windows;C:\\Windows\\System32\\Wbem;C:\\Program Files\\Eclipse Adoptium\\jre-11.0.16.101-hotspot\\bin;C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\;C:\\Program Files\\Git\\bin,C:\\Program Files\\Git\\cmd,C:\\Program Files\\Git\\usr\\bin'
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
        prepareFiles("borland")      
      }
    }
    stage('BUILD') {
      steps {
        script {
          switch (LABEL) {
            case ('cardpro') :
              borlandBuild("CARDLIB", "cardlib")
              borlandBuild("C:\\Windows\\System32", "32")
              borlandBuild("${WORKSPACE}", "${LABEL}")
              borlandBuild("FORM", "form")
            break
            case ['mmseod', 'palmerauloade', 'passkey'] :
              if (LABEL=='mmseod') 
              {borlandBuild('C:\\Program Files\\Borland\\CBuilder6\\Bin', 'bin')}
              borlandBuild("${WORKSPACE}", "${LABEL}")
            break
          }
        }
      }
    }
    stage('UPLOAD') {
      steps {
        uploadFiles("${JOB_BASE_NAME}", "${TARGET}")
      }
    }
  }//stages
  post {
    always { cleanWs() }
    failure { sendEmail() }
  }
}//pipeline