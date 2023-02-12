@Library("shared-library") _
pipeline { //CI-57
    agent {label 'jenkins-rosa'}
    environment {
        ROOT="NexusShareAsIs" //project root at SVN
        SVN_PATH ="${ROOT}" //full path for download fron SVN
    }
    stages {
        stage('SET Env') {
            steps {
                setDescription()
                setEnv()
            }
        }
        stage ('PREPARE') {
            steps { getSVN() }
        }
        stage('BUILD') {
            steps {
                dir(ROOT) {
                    script {
                    def nexus_creds = [
                    [path: 'secrets/creds/nexus', secretValues: [
                    [envVar: 'nexus_pwd', vaultKey: 'password']]]]
                    sh "find . -type d -name .svn -exec rm -rf {} +" //to delete ./svn folder recursively
                    loadScript(place:'linux', name:'scripts2nexusUpload.sh') //bash for curl to Nexus
                    lvl1 = listDir.Nix("${ROOT}")
                        lvl1.each { itLvl1-> lvl2=listDir.Nix("${ROOT}/${itLvl1}")
                            lvl2.each { itLvl2-> lvl3=listDir.Nix("${ROOT}/${itLvl1}/${itLvl2}")
                                lvl3.each { itLvl3->
                                wrap([$class: 'VaultBuildWrapper', vaultSecrets: nexus_creds]) {
                                sh "./scripts2nexusUpload.sh ${itLvl1} ${itLvl2} ${itLvl3}" } } } }
                    }
                }
            }
        }
    } //stages
    post {
        always { cleanWs() }
        failure { sendEmail() }
    }
} //pipeline