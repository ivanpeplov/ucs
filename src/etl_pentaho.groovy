import org.apache.commons.io.FilenameUtils
@Library("shared-library") _
pipeline { //CI-60
    agent {label 'jenkins-rosa'}
    environment {
        ROOT="TestSQLtoNexus" //project root at SVN 
        SVN_PATH ="${ROOT}" //full path for download fron SVN
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
                }
            }
        }
        stage('Extract Transform Load') {
            steps {
                dir (ROOT) {
                script {
                    def nexus_creds = [
                        [ path: 'secrets/creds/nexus', secretValues: [
                        [ envVar: 'nexus_pwd', vaultKey: 'password']]]]
                    //to delete junk /.svn folder recursively at lvl1
                    sh "find . -type d -name .svn -exec rm -rf {} +" 
                    lvl1 = listDir("${ROOT}") //level 1 - group folder [MNR19]
                    loadScript(place:'linux', name:'spaceToUnderscore.sh')
                    sh "./spaceToUnderscore.sh" //change " " to "_" in filenames recursively
                    loadScript(place:'linux', name:'pthUpload.sh') //bash script for upload .xml to Nexus
                    loadScript(place:'linux', name:'pthConversion.sh') //bash script for PTH conversion
                        for (io in lvl1) { lvl2 = listDir("${ROOT}/${io}")
                            lvl2=lvl2 - 'BIN' //[AMSBatch.PTH, BonusETL_top.PTH] - 'BIN'
                            for (jo in lvl2) { ext = FilenameUtils.getExtension(jo)
                              switch (ext) {
                                case ('PTH') :
                                stage=listDir("${ROOT}/${io}/${jo}")
                                if (stage != '') { //substage conversion
                                for (lo in stage) {
                                pthConversion (r:"${ROOT}", l1:"${io}", l2:"${jo}", ss:"${lo}") } }
                                //stage conversion
                                pthConversion (r:"${ROOT}", l1:"${io}", l2:"${jo}")
                                wrap([$class: 'VaultBuildWrapper', vaultSecrets: nexus_creds]) //upload to nexus
                                { sh "./pthUpload.sh ${io} ${jo}" }
                                break //PTH stage
                                default: println 'TBD'
                              } //switch EXT
                            } //loop lvl2 
                        } //loop lvl1
                    }
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
                echo 'emailing'
                sendEmail()               
            }//script
        }//failure
    } //post actions
} //pipeline
