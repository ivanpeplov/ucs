import org.apache.commons.io.FilenameUtils
def call(String path) {
    dir (path) { //path="TestSQLtoNexus"
        loadLinuxScript('pthConversion.sh')
        lvl1 = listDir("${WORKSPACE}/${path}") //level 1 - group folder
        println lvl1 //[MNR19]
        lvl2=[] //[AMSBatch.PTH, BIN, BonusETL_top.PTH, ETL_CDWH.PTH]
        exe=[] //[AMSBatch.PTH, BonusETL_top.PTH, ETL_CDWH.PTH]
        ext=[] //[PTH]
        sh "find . -type d -name .svn -exec rm -rf {} +" //to delete junk /.svn folder recursively from lvl1
        spaceToUnderscore() //change " " to "_" in filenames recursively
        loadLinuxScript('pthUpload.sh')
        for (int i = 0; i < lvl1.size(); i++) { //level 2 - define conversion folders
        lvl2[i] = listDir("${WORKSPACE}/${path}/${lvl1[i]}")
        bin=['BIN'] // remove BIN from lvl2 folders list
        exe[i]=lvl2[i] - bin
        println exe[i] // [AMSBatch.PTH, BonusETL_top.PTH, ETL_CDWH.PTH]
            for (int j=0; j < exe[i].size(); j++) {
                ext[j] = FilenameUtils.getExtension(exe[i][j]) //Get folder extension. PTH is current conversion label extension
                switch ("${ext[j]}") { // switch for different conversion scripts. currently active: PTH (.ktr to .xml)
                    case ('PTH'): // PTH conversion for Pentaho
                    //check for dirs at level 'exe[i]' than
                    stage=listDir("${WORKSPACE}/${path}/${lvl1[i]}/${exe[i][j]}")
                    if (stage != '') { //if target PTH folder has a subfolders
                        for (int l=0; l < stage.size(); l++) { //PTH has a subfolders
                            //get filename.ext list (.ktr/.kjb) inside each subfolder
                            substage_list = listFiles("${WORKSPACE}/${path}/${lvl1[i]}/${exe[i][j]}/${stage[l]}")
                            .findAll{it.toLowerCase().contains('.ktr') || it.toLowerCase().contains('.kjb')}
                            for (int m=0; m < substage_list.size(); m++) {
                                ext=[]
                                name=[]
                                println "${substage_list[m]}" //each .ktr/.kjb file at 'substage' folder under PTH folder
                                ext[m] = FilenameUtils.getExtension(substage_list[m]) //each .ktr/.kjb filename
                                name[m] = FilenameUtils.removeExtension(substage_list[m]) //each .ktr/.kjb extension
                                //main pentaho conversion .sh script
                                pthConversion ("${lvl1[i]}", "${exe[i][j]}", "${stage[l]}", "${name[m]}", "${ext[m]}")
                            }   
                        }
                    }       //as abobe actions but inside .PTH folder directly
                            stage_list = listFiles("${WORKSPACE}/${path}/${lvl1[i]}/${exe[i][j]}")
                            .findAll{it.toLowerCase().contains('.ktr') || it.toLowerCase().contains('.kjb')}
                            for (int k=0; k < stage_list.size(); k++) {
                                ext=[]
                                name=[]
                                println "${stage_list[k]}" //each .ktr/.kjb file under PTH folder
                                ext[k] = FilenameUtils.getExtension(stage_list[k]) //each .ktr/.kjb filename
                                name[k] = FilenameUtils.removeExtension(stage_list[k]) //each .ktr/.kjb extension
                                pthConversion ("${lvl1[i]}", "${exe[i][j]}", "", "${name[k]}", "${ext[k]}")
                                
                            }
                    pthUpload("${lvl1[i]}", "${exe[i][j]}")  //zip .log files, curl artifact, rm temp files at the each .PTH stage finish
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
