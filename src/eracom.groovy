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
    NODE='ERA'
    SVN_PATH='PassKey/FM/FmUX'
    SVN='trunk'
    VERSION=' '
    TARGET='fm\\obj-armfm'
    COMMAND='bat'
    PATH='c:\\jenkins\\bin;c:\\Windows\\System32;C:\\Program Files\\TortoiseSVN\\bin;c:\\jenkins\\bin;C:\\Windows\\system32;C:\\Windows;C:\\Windows\\System32\\Wbem;C:\\Program Files\\Eclipse Adoptium\\jre-11.0.16.101-hotspot\\bin;C:\\Windows\\System32\\WindowsPowerShell\\v1.0\\;C:\\Program Files\\Eracom\\PCI HSM\\bin;C:\\Program Files\\Eracom\\Network HSM\\bin;C:\\Program Files\\Eracom\\ProtectToolkit C SDK\\bin;C:\\Program Files\\Eracom\\ProtectToolkit C SDK\\bin\\sw;C:\\Program Files\\Eracom\\ProtectProcessing Orange SDK\\bin;c:\\gcc-fm\\bin;C:\\Program Files\\Java\\jre1.8.0_341\\bin;C:\\Program Files\\Git\\bin,C:\\Program Files\\Git\\cmd,C:\\Program Files\\Git\\usr\\bin'
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
                }
            }
        }
        stage('App Build') {
            steps {
                script {
                    echo "ERACOM FM build"
                    hsmMake('FmUX\\fm', 'era')
                }
            }
        }
        stage('App Sign') {
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
                    hsmSign('FmUX\\fm\\obj-armfm', 'era')
                }
            }
        }
        stage('Upload') {
            steps {
                script {
                    uploadFiles('era')
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