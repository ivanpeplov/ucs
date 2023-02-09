pipeline {
    agent {label 'OW'}
    parameters {
        choice(name: 'SVP', choices: ['skip', 'start', 'shutdown'], description: 'Reload operations for SVP hosts')
        choice(name: 'RDH', choices: ['skip','start', 'shutdown'], description: 'Reload operations for RDH hosts')
        choice(name: 'RG', choices: ['skip','enable', 'disable'], description: 'OFF/ON ext network for RG hosts')
        booleanParam(name: "SVP_TS", defaultValue: false, description: 'Kill active RDP sessions on SVP hosts')
        booleanParam(name: "RDH_TS", defaultValue: false, description: 'Kill active RDP sessions on RDH hosts')
        
    } //parameters end
    stages {
        // Stage 1 (Set Global Env)
        stage('Set Global Env') {
            steps {
                script {
                        patch = load "${WORKSPACE}/lib/libpatch.groovy"
                        tools = load "${WORKSPACE}/lib/libtools.groovy"
                }
            }
        }
        // Stage 2 (Settings)
        stage('Settings') {
            steps {
                script {
                    patch.set_env()
                    patch.prepare()
                }
            }
        }
        /* 
            Modification ALERT
            ALL Preliminary steps completed !
            Next pipeline stages are modifying data !
        */  
        // Stage 2 (POWER OFF SVP)
        stage('SVP hosts operations') {
            steps {
                script {
                    switch(SVP) {
                        case ["shutdown"]:
                            echo 'stop SVP'
                            patch.power('shutdown','svp')
                        break
                        case ["start"]:
                            echo 'start SVP'
                            patch.power('start','svp')
                        default:
                            println('Skip SVP operations')
                        break
                    }
                }
            }
        }
        // Stage 3 (POWER OFF RDH)
        stage('RDH hosts operations') {
            steps {
                script {
                    switch(RDH) {
                        case ["shutdown"]:
                            echo 'stop RDH'
                            patch.power('shutdown','rdh')
                        break
                        case ["start"]:
                            echo 'start RDH'
                            patch.power('start','rdh')
                        default:
                            println('Skip RDH operations')
                        break
                    }
                }
            }
        }
        // Stage 4 (DISABLE RG ETHERNET)
        stage('RG hosts operations') {
            steps {
                script {
                    switch(RG) {
                        case ["disable"]:
                            echo 'disable RG gate'
                            patch.gates('disconnect','rg')
                        break
                        case ["enable"]:
                            echo 'enable RG gate'
                            patch.gates('connect','rg')
                        default:
                            println('Skip RG operations')
                        break
                    }
                }
            }
        }
        // Stage 5 (Kill SVP sessions)
        stage('Kill SVP sessions') {
             when {
                expression { return env.SVP_TS.toBoolean() }
                }
            steps {
                script {
                    echo 'stop SVP active sessions'
                    patch.rdp('tmp')
                }
            }
        }
        // Stage 6 (Kill RDH sessions)
        stage('Kill RDH sessions') {
             when {
                expression { return env.RDH_TS.toBoolean() }
                }
            steps {
                script {
                    echo 'stop RDH active sessions'
                    //patch.rdp('tmp')
                }
            }
        }
    } //stages
    post {
	    always { cleanWs() }
	    failure { sendEmail() }
    }
} //pipeline
