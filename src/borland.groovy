@Library("shared-library") _
properties([
  parameters([
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'Select Borland Build:  MMS_EOD,  PALMERA loader,  TID manager',
      name: 'LABEL', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: 
            'return["mms_eod", "palmera", "tid_man"]'
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
          if (LABEL=='mms_eod') {return["MMS/mmsEOD"]}
          if (LABEL=='palmera') {return["Util/PalmeraLoader"]}
          if (LABEL=='tid_man') {return["CardPro/TidManager/TID_v6"]}
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
          if (LABEL=='mms_eod') {return["MMS"]}
          if (LABEL=='palmera') {return["PALMERA"]}
          if (LABEL=='tid_man') {return["TID"]}
          '''
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'Select Trunk, Branches or Tags',
      name: 'SVN', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: 
            'return["trunk", "branches", "tags"]'
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
pipeline { //CI-56,58,59
  agent {label 'borland'}
  environment {
    TARGET = "${WORKSPACE}\\UPLOAD" //target folder for binaries
    SVN_PATH = "${ROOT}/${SVN}/${VERSION}" //full path for download fron SVN
    PATH='C:\\Program Files\\Borland\\CBuilder6\\Bin;C:\\Program Files\\Borland\\CBuilder6\\Projects\\Bpl;c:\\jenkins\\bin;c:\\Windows\\System32;C:\\Program Files\\TortoiseSVN\\bin;c:\\jenkins\\bin;C:\\Windows\\system32;C:\\Windows;C:\\Windows\\System32\\Wbem;C:\\Program Files\\Eclipse Adoptium\\jre-11.0.16.101-hotspot\\bin;C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\;C:\\Program Files\\Git\\bin,C:\\Program Files\\Git\\cmd,C:\\Program Files\\Git\\usr\\bin'
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
          prepareFiles("${JOB_BASE_NAME}")      
        }
      }
    }
    stage('BUILD') {
      steps {
        script {
          switch (LABEL) {
            case ('mms_eod') :
              makeBorland("${WORKSPACE}", 'mms_eod')
              makeBorland('C:\\Program Files\\Borland\\CBuilder6\\Bin', 'bin')
            break
            case ('palmera') :
              makeBorland("${WORKSPACE}", 'palmera')
            break
            case ('tid_man') :
              makeBorland("CARDLIB", 'lib')
              makeBorland('FORM', 'form')
              makeBorland("${WORKSPACE}", 'tid_man')
              makeBorland('C:\\Windows\\System32', '32')
              makeBorland('FORM\\PRINT.CFG', 'print')
            break
          }
        }
      }
    }
    stage('UPLOAD') {
      steps {
        script {
          uploadFiles("${JOB_BASE_NAME}", "${TARGET}")
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