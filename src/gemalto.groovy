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
      description: 'd for obj-$(ARCH)d : $(OBJDIR) folder',
      filterLength: 1,
      filterable: false,
      referencedParameters: 'DEBUG',
      name: 'DIR', 
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
    agent {label 'master'}
    options {
        timeout(time: 10, unit: 'MINUTES') 
    }
    parameters {
    booleanParam(name: "SIGN", defaultValue: false, description: 'Only for testing')
    } //parameters end
    environment {
    NODE='GEM'
    SVN='trunk'
    TARGET="FmUX/fm/obj-ppcfm${DIR}"
    SVN_PATH='PassKey/FM/FmUX'
    VERSION=' '
    CPROVDIR='/opt/safenet/protecttoolkit5/ptk'
    FMDIR='/opt/safenet/protecttoolkit5/fmsdk'
    LD_LIBRARY_PATH="/opt/safenet/protecttoolkit5/ptk/lib:"
    PATH="/opt/safenet/protecttoolkit5/ptk/bin:/usr/local/bin:/usr/bin:/bin:"
    PTKBIN="/opt/safenet/protecttoolkit5/ptk/bin"
    PTKLIB="/opt/safenet/protecttoolkit5/ptk/lib"
    PTKMAN="/opt/safenet/protecttoolkit5/ptk/man"
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
            agent {label NODE}
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
                    echo "GEMALTO FM Build"
                    hsmMake('FmUX/fm', 'gemalto')
                }
            }
        }
        stage('SIGN') {
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
                    hsmSign("${TARGET}", "gem")
                }
            }
        }
        stage('UPLOAD') {
            steps {
                script {
                    uploadFiles('gemalto')
                }
            }
        }
    } //stages
        post {
            always {
                script {             
                    echo 'Clean Workspace'
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