import org.apache.commons.io.FilenameUtils
def call(String path) {
    dir (path) { //path="TestSQLtoNexus"
        lvl1 = listDir("${WORKSPACE}/${path}") //level 1 - group folder
        println lvl1 //[MNR19]
        lvl2=[] //[AMSBatch.PTH, BIN, BonusETL_top.PTH, ETL_CDWH.PTH]
        exe=[] //[AMSBatch.PTH, BonusETL_top.PTH, ETL_CDWH.PTH]
        ext=[] //[PTH]
        //to delete junk /.svn folder recursively from lvl1
        sh "find . -type d -name .svn -exec rm -rf {} +"
        //level 2 - define conversion folders
        for (int i = 0; i < lvl1.size(); i++) {
        lvl2[i] = listDir("${WORKSPACE}/${path}/${lvl1[i]}")
        bin=['BIN'] // remove BIN from lvl2 folders list
        exe[i]=lvl2[i] - bin
        println exe[i] // [AMSBatch.PTH, BonusETL_top.PTH, ETL_CDWH.PTH]
            for (int j=0; j < exe[i].size(); j++) {
            //Get folder extension. PTH is current conversion label extension
                ext[j] = FilenameUtils.getExtension(exe[i][j])
                // switch for different conversion scripts. currently active: PTH (.ktr to .xml)
                switch ("${ext[j]}") {
                    case ('PTH'): // PTH conversion for Pentaho
                    //check for dirs at level 'exe[i]' than
                    stage=listDir("${WORKSPACE}/${path}/${lvl1[i]}/${exe[i][j]}")
                    if (stage != '') { //if target PTH folder has a subfolders
                        for (int l=0; l < stage.size(); l++) { //PTH has a subfolders
                            //get filename.ext list (.ktr/.kjb) inside each subfolder
                            substage_list = listFiles("${WORKSPACE}/${path}/${lvl1[i]}/${exe[i][j]}/${stage[l]}").findAll{it.toLowerCase().contains('.ktr') || it.toLowerCase().contains('.kjb')}
                            for (int m=0; m < substage_list.size(); m++) {
                                ext=[]
                                name=[]
                                println "${substage_list[m]}" //each .ktr/.kjb file at subfolder under PTH
                                ext[m] = FilenameUtils.getExtension(substage_list[m]) //each .ktr/.kjb filename
                                name[m] = FilenameUtils.removeExtension(substage_list[m]) //each .ktr/.kjb extension
                                //main pentaho conversion .sh script
                                pthConversion ("${lvl1[i]}", "${exe[i][j]}", "${stage[l]}", "${name[m]}", "${ext[m]}")

                            }   
                        }
                    }       //as abobe actions but inside .PTH folder directly
                            stage_list = listFiles("${WORKSPACE}/${path}/${lvl1[i]}/${exe[i][j]}").findAll{it.toLowerCase().contains('.ktr') || it.toLowerCase().contains('.kjb')}
                            for (int k=0; k < stage_list.size(); k++) {
                                ext=[]
                                name=[]
                                println "${stage_list[k]}" //each .ktr/.kjb file under PTH
                                ext[k] = FilenameUtils.getExtension(stage_list[k]) //each .ktr/.kjb filename
                                name[k] = FilenameUtils.removeExtension(stage_list[k]) //each .ktr/.kjb extension
                                pthConversion ("${lvl1[i]}", "${exe[i][j]}", '', "${name[k]}", "${ext[k]}")
                            }
                    //zipping .log files, curl artifact to repo, rm temp files at the each .PTH stage finish
                    pthUpload("${lvl1[i]}", "${exe[i][j]}")
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
