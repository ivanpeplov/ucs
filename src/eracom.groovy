@Library("shared-library") _
pipeline {
    agent {label 'master'}
    options {
        timeout(time: 10, unit: 'MINUTES') 
    }
    parameters {
    booleanParam(name: "SIGN", defaultValue: false, description: 'Only for test variant')
    choice(name: 'DIR', choices: ['', 'd'], description: 'DEBUG: d for obj-$(ARCH)d : $(OBJDIR) folder')
     
    } //parameters end
    environment {
    NODE='ERA'
    TARGET="FmUX/fm/obj-armfm${DIR}" //where find files for upload
    ROOT='PassKey/FM/FmUX' //project root at SVN
    SVN='trunk' //only for setDescription()
    VERSION=' ' //only for setDescription()
    SVN_PATH = "${ROOT}" //full path for download fron SVN
    PATH='c:\\jenkins\\bin;c:\\Windows\\System32;C:\\Program Files\\TortoiseSVN\\bin;c:\\jenkins\\bin;C:\\Windows\\system32;C:\\Windows;C:\\Windows\\System32\\Wbem;C:\\Program Files\\Eclipse Adoptium\\jre-11.0.16.101-hotspot\\bin;C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\;C:\\Program Files\\Eracom\\PCI HSM\\bin;C:\\Program Files\\Eracom\\Network HSM\\bin;C:\\Program Files\\Eracom\\ProtectToolkit C SDK\\bin;C:\\Program Files\\Eracom\\ProtectToolkit C SDK\\bin\\sw;C:\\Program Files\\Eracom\\ProtectProcessing Orange SDK\\bin;c:\\gcc-fm\\bin;C:\\Program Files\\Java\\jre1.8.0_341\\bin;C:\\Program Files\\Git\\bin,C:\\Program Files\\Git\\cmd,C:\\Program Files\\Git\\usr\\bin'
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
                    prepareFiles('eracom')
                }
            }
        }
        stage('BUILD') {
            steps {
                script {
                    echo "ERACOM FM build"
                    hsmMake('FmUX\\fm', 'eracom')
                }
            }
        }
        stage('SIGN') {
            when {
                    beforeInput true
                    expression { return env.SIGN.toBoolean() }
                }
                input {
                message "PWD for ERACOM FM Sign-in"
                ok "Yes"
                parameters {
                    string(name: "ERA_SIGN", defaultValue: "eracom")
                }
            }
            steps {
                script {
                    echo "Sign pwd for ERACOM: ${ERA_SIGN}"
                    hsmSign("${TARGET}", 'eracom')
                }
            }
        }
        stage('UPLOAD') {
            steps {
                script {
                    uploadFiles('eracom', "${TARGET}")
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