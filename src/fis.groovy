@Library("shared-library") _
properties([
  parameters([
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'Select:  FIS server,  FIS utility',
      name: 'LABEL', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: 
            'return["fis", "fis_util"]'
        ]
      ]
    ],
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
      referencedParameters: 'LABEL',
      name: 'TARGET', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
          if (LABEL=='fis') {return["bin/fis.bin"]}
          if (LABEL=='fis_util') {return["bin"]}
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
      description: 'Select Trunk or Tags (opcpposcv only for Trunk)',
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
      referencedParameters: 'LABEL',
      name: 'SAMPLES', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
          if (LABEL=='fis') {return["all","by one"]}
          if (LABEL=='fis_util') {return["by one"]}
          '''
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_CHECKBOX', 
      description: 'Select: (opcpposcv only for Trunk)',
      referencedParameters: 'SAMPLES, LABEL',
      name: 'MODULES', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
            svn_url='172.16.10.230/scm/svn/dev'
            proj='FIS/new/trunk/units'
            if (SAMPLES == "all" && LABEL == "fis") { return ["all:selected:disabled"] }
            else {
            if (SAMPLES == "by one" && LABEL == "fis") {
            proc1= ["bash", "-c", "svn list --username jenkins --password mRovmZVpt  https://${svn_url}/${proj}/fis/samples"].execute()
            proc2= ["bash", "-c", "rev | cut -c2- | rev"].execute()
            proc3= ["bash", "-c", "tail -n +2"].execute()
            all = proc1 | proc2 | proc3
            choices = all.text.split().toList()
            return choices }
            if (SAMPLES == "by one" && LABEL == "fis_util") {
            noList=['baselib', 'fis', 'mqlib']
            proc1= ["bash", "-c", "svn list --username jenkins --password mRovmZVpt  https://${svn_url}/${proj}"].execute()
            proc2= ["bash", "-c", "rev | cut -c2- | rev"].execute()
            all = proc1 | proc2
            choices = all.text.split().toList() - noList
            return choices }            
            }
            '''
        ]
      ]
    ]
  ])
])
pipeline { //CI-51
    agent {label NODE_NAME}
    environment {
      ROOT='FIS/new' //project root at SVN
      TOOR='FIS' //project root at NEXUS
      SVN_PATH = "${ROOT}/${SVN}/${VERSION}/units" //full path for download fron SVN
      //environment for build
      PROJECTS="/home/jenkins/workspace/${JOB_NAME}" //Not use ${WORKSPACE} here
      PATH="${PATH}:${PROJECTS}/tools:${PROJECTS}/units:${PROJECTS}/bin"
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
      stage('FIS') {
        when { expression  { LABEL == "fis" } }
        steps {
          dir ('units/fis/samples/') {
            script {
            units = MODULES.split(',').toList()
            units.each {f -> sh "cd ${f} ; Make ${RELEASE}"}
            }
          }
        }
      }
      stage('FIS util') {
        when { expression  { LABEL == "fis_util" } }
        steps {
          dir ('units') {
            script {
            units = MODULES.split(',').toList()
            units.each {f -> sh "cd ${f} ; Make ${RELEASE}"}
            }
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