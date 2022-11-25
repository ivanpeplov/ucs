import org.apache.commons.io.FilenameUtils
def call(String path) {
    dir (path) { //path="TestSQLtoNexus"
        lvl1 = listDir("${WORKSPACE}/${path}") //level 1 - group folder
        println lvl1 //[MNR19]
        lvl2=[] //[AMSBatch.PTH, BIN, BonusETL_top.PTH, ETL_CDWH.PTH]
        exe=[] //[AMSBatch.PTH, BonusETL_top.PTH, ETL_CDWH.PTH]
        ext=[] //[PTH]
        sh "find . -type d -name .svn -exec rm -rf {} +" //to delete junk /.svn folder recursively from lvl1
        loadLinuxScript('spaceToUnderscore.sh')
        sh "./spaceToUnderscore.sh" //change " " to "_" in filenames recursively
        loadLinuxScript('pthUpload.sh') //load bash script for upload .xml to Nexus
        loadLinuxScript('pthConversion.sh') //load bash script for PTH conversion
        for (int i = 0; i < lvl1.size(); i++) { //level 2 - define conversion folders
        lvl2[i] = listDirNix("${WORKSPACE}/${path}/${lvl1[i]}")
        bin=['BIN'] // remove BIN from lvl2 folders list
        exe[i]=lvl2[i] - bin
        println exe[i] // [AMSBatch.PTH, BonusETL_top.PTH, ETL_CDWH.PTH]
            for (int j=0; j < exe[i].size(); j++) {
                ext[j] = FilenameUtils.getExtension(exe[i][j]) //Get folder extension. PTH is current conversion label extension
                switch ("${ext[j]}") { // switch for different conversion scripts. currently active: PTH (.ktr to .xml)
                    case ('PTH'): // PTH conversion for Pentaho
                    //check for dirs at level 'exe[i]' than
                    stage=listDirNix("${WORKSPACE}/${path}/${lvl1[i]}/${exe[i][j]}")
                    if (stage != '') { //if target PTH folder has a subfolders
                        for (int l=0; l < stage.size(); l++) { //PTH has a subfolders
                            //get filename.ext list (.ktr/.kjb) inside each subfolder
                            substage_list = listFilesNix("${WORKSPACE}/${path}/${lvl1[i]}/${exe[i][j]}/${stage[l]}")
                            .findAll{it.toLowerCase().contains('.ktr') || it.toLowerCase().contains('.kjb')}
                            for (int m=0; m < substage_list.size(); m++) {
                                ext=[]
                                name=[] //each .ktr/.kjb file at 'substage' folder under PTH folder
                                ext[m] = FilenameUtils.getExtension(substage_list[m]) //each .ktr/.kjb filename
                                name[m] = FilenameUtils.removeExtension(substage_list[m]) //each .ktr/.kjb extension
                                //main pentaho conversion .sh script
                                sh "./pthConversion.sh ${lvl1[i]} ${exe[i][j]} ${name[m]} ${ext[m]} ${stage[l]}" //include stage (5 parameters)
                            }   
                        }
                    }       //as abobe actions but inside .PTH folder directly
                            stage_list = listFilesNix("${WORKSPACE}/${path}/${lvl1[i]}/${exe[i][j]}")
                            .findAll{it.toLowerCase().contains('.ktr') || it.toLowerCase().contains('.kjb')}
                            for (int k=0; k < stage_list.size(); k++) {
                                ext=[]
                                name=[] //each .ktr/.kjb file under PTH folder
                                ext[k] = FilenameUtils.getExtension(stage_list[k]) //each .ktr/.kjb filename
                                name[k] = FilenameUtils.removeExtension(stage_list[k]) //each .ktr/.kjb extension
                                sh "./pthConversion.sh ${lvl1[i]} ${exe[i][j]} ${name[k]} ${ext[k]}" //without stage (4 parameters)
                            }
                    def nexus_creds = [
                    [path: 'secrets/creds/nexus', secretValues: [
                    [envVar: 'nexus_pwd', vaultKey: 'password']]]]
                    wrap([$class: 'VaultBuildWrapper', vaultSecrets: nexus_creds]) {
                    sh "./pthUpload.sh ${lvl1[i]} ${exe[i][j]}" }
                    break //PTH stage finished
                    case ('ATH'): //future extensions; fake ATH
                        println 'to be define ATH method'
                    break
                    default:
                        println 'TBD'
                } //switch for .PTH/.ATH/TBD extensions finished
            } //loop for lvl2  [AMSBatch.PTH, BonusETL_top.PTH, ETL_CDWH.PTH] finished
        } //loop for lvl1 [MNR19] finished 
    } //end of dir () closure
}//end of def call()
