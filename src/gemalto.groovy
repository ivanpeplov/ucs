@Library("shared-library") _
pipeline {
    agent {label 'master'}
    options {
        timeout(time: 10, unit: 'MINUTES') 
    }
    parameters {
    booleanParam(name: "SIGN", defaultValue: false, description: 'Only for test variant')

        
    } //parameters end
    environment {
    NODE='GEM'
    SVN='trunk'
    SVN_PATH='PassKey/FM/FmUX'
    VERSION=' '
    TARGET='FmUX/fm/obj-ppcfm' //target folder for binaries
    CPROVDIR='/opt/safenet/protecttoolkit5/ptk'
    FMDIR='/opt/safenet/protecttoolkit5/fmsdk'
    LD_LIBRARY_PATH="${CPROVDIR}/lib:"
    PATH="${CPROVDIR}/bin:/usr/local/bin:/usr/bin:/bin:"
    PTKBIN="${CPROVDIR}/bin"
    PTKLIB="${CPROVDIR}/lib"
    PTKMAN="${CPROVDIR}/man"
    }
    stages {
        stage('Set Env') {
            steps {
                script {
                    setDescription()
                    setEnv()
                }
            }
        }
        stage ('Prepare') {
            agent {label NODE}
            steps {
                script {
                    getSVN()
                    prepareFiles('gem')
                }
            }
        }
        stage('App Build') {
            steps {
                script {
                    echo "GEMALTO FM Build"
                    hsmMake('FmUX/fm', 'gem')
                }
            }
        }
        stage('App Sign') {
            when {
                    beforeInput true
                    expression { return env.SIGN.toBoolean() }
                }
                input {
                message "PWD for GEMALTO FM Sign-in"
                ok "Yes"
                parameters {
                    string(name: "GEM_SIGN", defaultValue: "gemalto")
                }
            }
            steps {
                script {
                    echo "Sign pwd for GEMALTO: ${GEM_SIGN}"
                    hsmSign('FmUX/fm/obj-ppcfm', 'gem')
                }
            }
        }
        stage('Upload') {
            steps {
                script {
                    uploadFiles('gem')
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