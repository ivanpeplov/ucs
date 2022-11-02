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
            svn_ip='172.16.10.230'
            svn_url='scm/svn/dev'
            proj_root='FIS/new'
            if (SVN == "tags") {
            proc1= ["bash", "-c", "svn list --username jenkins --password mRovmZVpt  https://${svn_ip}/${svn_url}/${proj_root}/${SVN}"].execute()
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
pipeline {
    agent {label 'master'}
    options {
        timeout(time: 10, unit: 'MINUTES') 
    }
    parameters {
        choice(name: 'NODE', choices: ['LEGACY', 'ROSA'], description: 'The node to run on')
        booleanParam(name: "LIB_UPLOAD", defaultValue: false, description: 'If checked, will add LIBFIS.a/BASELIB.a/LIBMQLIB.a to artifact.zip')
        choice(name: 'RELEASE', choices: ['release', 'debug'], description: '')
        booleanParam(name: "ALL_ITEMS", defaultValue: false, description: 'If true, builds all modules below')
        booleanParam(name: "FILETIME", defaultValue: false)
        booleanParam(name: "FIOPORT", defaultValue: false)
        booleanParam(name: "FISMEM", defaultValue: false)
        booleanParam(name: "FISMON", defaultValue: false)
        booleanParam(name: "FMCOMPARE", defaultValue: false)
        booleanParam(name: "FMDIFF", defaultValue: false)
        booleanParam(name: "POSB", defaultValue: false)
        booleanParam(name: "SCRIPTS", defaultValue: false)
        booleanParam(name: "WATCHFILE", defaultValue: false)
        booleanParam(name: "XTR2XML", defaultValue: false)
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
        stage('FILETIME') {
            when {
                anyOf {
                    expression { return env.FILETIME.toBoolean() }
                    expression { return env.ALL_ITEMS.toBoolean() }
                }
            }
            steps {
                script {
                    prjMakeOld('units/fis/samples/filetime')
                }
            }
        }
        stage('FIOPORT') {
            when {
                anyOf {
                    expression { return env.FIOPORT.toBoolean() }
                    expression { return env.ALL_ITEMS.toBoolean() }
                }
            }
            steps {
                script {
                    prjMakeOld('units/fis/samples/fioport')
                }
            }
        }
        stage('FISMEM') {
            when {
                anyOf {
                    expression { return env.FISMEM.toBoolean() }
                    expression { return env.ALL_ITEMS.toBoolean() }
                }
            }
            steps {
                script {
                    prjMakeOld('units/fis/samples/fismem')
                }
            }
        }
        stage('FISMON') {
            when {
                anyOf {
                    expression { return env.FISMON.toBoolean() }
                    expression { return env.ALL_ITEMS.toBoolean() }
                }
            }
            steps {
                script {
                    prjMakeOld('units/fis/samples/fismon')
                }
            }
        }
        stage('FMCOMPARE') {
            when {
                anyOf {
                    expression { return env.FMCOMPARE.toBoolean() }
                    expression { return env.ALL_ITEMS.toBoolean() }
                }
            }
            steps {
                script {
                    prjMakeOld('units/fis/samples/fmcompare')
                }
            }
        }
        stage('FMDIFF') {
             when {
                anyOf {
                    expression { return env.FMDIFF.toBoolean() }
                    expression { return env.ALL_ITEMS.toBoolean() }
                }
            }
            steps {
                script {
                    prjMakeOld('units/fis/samples/fmdiff')
                }
            }
        }
        stage('POSB') {
            when {
                anyOf {
                    expression { return env.POSB.toBoolean() }
                    expression { return env.ALL_ITEMS.toBoolean() }
                }
            }
            steps {
                script {
                    prjMakeOld('units/fis/samples/posb')
                }
            }
        }
        stage('SCRIPTS') {
            when {
                anyOf {
                    expression { return env.SCRIPTS.toBoolean() }
                    expression { return env.ALL_ITEMS.toBoolean() }
                }
            }
            steps {
                script {
                    prjMakeOld('units/fis/samples/scripts')
                }
            }
        }
        stage('WATCHFILE') {
            when {
                anyOf {
                    expression { return env.WATCHFILE.toBoolean() }
                    expression { return env.ALL_ITEMS.toBoolean() }
                }
            }
            steps {
                script {
                    prjMakeOld('units/fis/samples/watchfile')
                }
            }
        }
        stage('XTR2XML') {
            when {
                anyOf {
                    expression { return env.XTR2XML.toBoolean() }
                    expression { return env.ALL_ITEMS.toBoolean() }
                }
            }
            steps {
                script {
                    prjMakeOld('units/fis/samples/xtr2xml')
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