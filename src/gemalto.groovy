@Library("shared-library") _
properties([
  parameters([
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'Select TRUE for make debug=1 Build',
      filterLength: 1,
      filterable: false,
      name: 'DEBUG', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: 
            'return ["", "TRUE"]'
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'obj-$(ARCH)d : $(OBJDIR) folder',
      filterLength: 1,
      filterable: false,
      referencedParameters: 'DEBUG',
      name: 'TAIL', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
            if (DEBUG == "TRUE") { return ["d:selected:disabled"] }
            '''
        ]
      ]
    ]
  ])
])
pipeline {
    agent {label 'jenkins-gem'}
    options { timeout(time: 10, unit: 'MINUTES') }
    parameters {
        //booleanParam(name: "SIGN", defaultValue: false, description: 'Only for testing')
    } //parameters end
    environment {
        TARGET="FmUX/fm/obj-ppcfm${TAIL}" //where find files for upload
        ROOT='PassKey/FM/FmUX' //project root at SVN
        SVN_PATH = "${ROOT}" //full path for download fron SVN
        //environment for build
        CPROVDIR='/opt/safenet/protecttoolkit5/ptk'
        FMDIR='/opt/safenet/protecttoolkit5/fmsdk'
        LD_LIBRARY_PATH="${CPROVDIR}/lib:"
        PATH="${CPROVDIR}/bin:/usr/local/bin:/usr/bin:/bin:"
        //PTKBIN="${CPROVDIR}/bin"
        //PTKLIB="${CPROVDIR}/lib"
        //PTKMAN="${CPROVDIR}/man"
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
                    prepareFiles('gemalto')
                }
            }
        }
        stage('BUILD') {
            steps {
                script {
                    hsmMakeOld('FmUX/fm', 'gemalto')
                }
            }
        }
        /*stage('SIGN') {
            when {
                beforeInput true
                expression { return env.SIGN.toBoolean() }
            }
            input {
                message "PWD for GEMALTO FM Sign-in"
                ok "Yes"
                parameters {
                    string(name: "GEM_SIGN", defaultValue: "222222")
                }
            }
            steps {
                script {
                    echo "Sign pwd for GEMALTO: ${GEM_SIGN}"
                    hsmSign("${TARGET}", "gemalto")
                }
            }
        }*/
        stage('UPLOAD') {
            steps {
                script {
                    uploadFiles('gemalto', "${TARGET}")
                }
            }
        }
    } //stages
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
                sendEmail()               
            }//script
        }//failure
    } //post actions
} //pipeline