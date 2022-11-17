@Library("shared-library") _
properties([
  parameters([
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'Select node to run',
      name: 'RELEASE', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script:
          'return["release", "debug"]'
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'Select',
      referencedParameters: 'RELEASE',
      name: 'CLEAR', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
            switch(RELEASE) {
            case ('release') :
            return ["rclear:selected:disabled"]
            break
            default :
            return ["dclear:selected:disabled"]
            }
            '''
        ]
      ]
    ],
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
          label='FIS'
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
      description: 'Select Trunk, Branches or Tags',
      name: 'SVN', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: 
            'return["trunk", "tags"]'
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'Select Version for Tags/Branches',
      referencedParameters: 'SVN',
      name: 'VERSION', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
            svn_url='172.16.10.230/scm/svn/dev'
            proj='FIS/new'
            if (SVN == "tags") {
            proc1= ["bash", "-c", "svn list --username jenkins --password mRovmZVpt  https://${svn_url}/${proj}/${SVN}"].execute()
            proc2= ["bash", "-c", "rev | cut -c2- | rev"].execute()
            all = proc1 | proc2
            choices = all.text
            return choices.split().toList()
            }
            '''
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description:'',
      name: 'SAMPLES', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: 
            'return["all","by one"]'
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_CHECKBOX', 
      description: 'Select',
      referencedParameters: 'SAMPLES',
      name: 'MODULES', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
            svn_url='172.16.10.230/scm/svn/dev'
            proj='FIS/new/trunk/units/fis'
            if (SAMPLES == "all") { return ["all:selected:disabled"] }
            else {
            proc1= ["bash", "-c", "svn list --username jenkins --password mRovmZVpt  https://${svn_url}/${proj}/samples"].execute()
            proc2= ["bash", "-c", "rev | cut -c2- | rev"].execute()
            proc3= ["bash", "-c", "tail -n +2"].execute()
            all = proc1 | proc2 | proc3
            choices = all.text
            return choices.split().toList()
            }
            '''
        ]
      ]
    ]
  ])
])
pipeline { //CI-51
    agent {label NODE_NAME}
    options { timeout(time: 10, unit: 'MINUTES') }
    environment {
      TARGET='bin/fis.bin' //where find files for upload
      ROOT='FIS/new' //project root at SVN
      SVN_PATH = "${ROOT}/${SVN}/${VERSION}/units" //full path for download fron SVN
      //environment for build
      PROJECTS="/home/jenkins/workspace/${JOB_NAME}" //Not use ${WORKSPACE} here
      INFORMIXSERVER="shlag"
      INFORMIXDIR="/opt/informix"
      INFORMIXSQLHOSTS="${INFORMIXDIR}/etc/sqlhosts"
      LD_LIBRARY_PATH="${INFORMIXDIR}/lib:${INFORMIXDIR}/lib/esql"
      DB_LOCALE="ru_ru.1251"
      CLIENT_LOCALE="ru_ru.1251"
      DBMONEY="."
      INCLUDE="-I. -I${PROJECTS}/units -I./include -I../include"
      LIB='-L${PROJECTS}/lib'
      MQCCSID=1251
      MQM="/opt/mqm"
    }
    stages {
      stage('SET Env') {
        steps {
          script {
            catchErrors()
            setDescription()
            setEnv()
          }
        }
      }
      stage ('PREPARE') {
        steps {
          script {
            getSVN()
            prepareFiles('fis')
          }
        }
      }
      stage('BUILD') {
        steps {
          script {
            fisMake('units/fis/samples/')
          }
        }
      }
      stage('UPLOAD') {
        steps {
          script {
            uploadFiles('fis', "${TARGET}")
          }
        }
      }
    } //stages
    post {
      always {
        script {             
            echo "Clean Workspace"
            cleanWs()
        }//script
      }//always
      failure {
        script {
          //emailing
          sendEmail()               
        }//script
      }//failure
    } //post actions
} //pipeline