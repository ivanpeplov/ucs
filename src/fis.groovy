@Library("shared-library") _
properties([
  parameters([
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'Select Trunk, Branches or Tags',
      filterLength: 1,
      filterable: false,
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
      filterLength: 1,
      filterable: false,
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
      filterLength: 1,
      filterable: false,
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
      filterLength: 1,
      filterable: false,
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
pipeline {
    agent {label 'master'}
    options {
        timeout(time: 10, unit: 'MINUTES') 
    }
    parameters {
        choice(name: 'NODE', choices: ['LEGACY', 'ROSA'], description: 'The node to run on')
        booleanParam(name: "LIB_UPLOAD", defaultValue: false, description: 'If checked, will add LIBFIS.a/BASELIB.a/LIBMQLIB.a to artifact.zip')
        choice(name: 'RELEASE', choices: ['release', 'debug'], description: '')
    } //parameters end
    environment {
    TARGET='bin' //target folder for binaries
    PROJ_ROOT='FIS/new'
    SVN_PATH = "${PROJ_ROOT}/${SVN}/${VERSION}/units"
    PROJECTS="/home/jenkins/workspace/${JOB_NAME}"
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
        stage('SET ENV') {
            steps {
                script {
                    setDescription()
                    setEnv()
                }
            }
        }
        stage ('PREPARE') {
            agent {label NODE}
            steps {
                script {
                    getSVN()
                    prepareFiles('fis')
                }
            }
        }
        stage('BUILDING UNITS') {
            steps {
                script {
                    prjMake('units/fis/samples/')
                }
            }
        }
        stage('UPLOAD ARTIFACT') {
            steps {
                script {
                    uploadFiles('nix')
                }
            }
        }
    } //stages
        post {
            always {
                script {             
                    // Clean Workspace
                    clearSlave()
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