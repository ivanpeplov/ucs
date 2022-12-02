import org.apache.commons.io.FilenameUtils
def call(String path) { //v2.0 01.12.2022
    dir (path) { //path="TestSQLtoNexus"
        sh "find . -type d -name .svn -exec rm -rf {} +" //to delete junk /.svn folder recursively at lvl1
        lvl1 = listDir("${path}") //level 1 - group folder [MNR19]
        loadScript(place:'linux', name:'spaceToUnderscore.sh')
        sh "./spaceToUnderscore.sh" //change " " to "_" in filenames recursively
        loadScript(place:'linux', name:'pthUpload.sh') //bash script for upload .xml to Nexus
        loadScript(place:'linux', name:'pthConversion.sh') //bash script for PTH conversion
        for (io in lvl1) { lvl2 = listDir("${path}/${io}")
            exe=lvl2 - 'BIN' //[AMSBatch.PTH, BonusETL_top.PTH, ETL_CDWH.PTH]
            for (jo in exe) {ext = FilenameUtils.getExtension(jo)
                switch (ext) {
                case ('PTH') :
                    stage=listDir("${path}/${io}/${jo}")
                    if (stage != '') {
                        for (lo in stage) {
                        substage_list = listFiles("${path}/${io}/${jo}/${lo}")
                        .findAll{it.toLowerCase().contains('.ktr') || it.toLowerCase().contains('.kjb')}
                            for (mo in substage_list) {
                            ext  = FilenameUtils.getExtension(mo) //each .ktr/.kjb filename
                            name = FilenameUtils.removeExtension(mo) //each .ktr/.kjb extension
                            sh "./pthConversion.sh ${io} ${jo} ${name} ${ext} ${lo}" } //incl. stage (5 parameters)
                        }
                    }
                        stage_list = listFiles("${path}/${io}/${jo}")
                        .findAll{it.toLowerCase().contains('.ktr') || it.toLowerCase().contains('.kjb')}
                        for (ko in stage_list) {
                            ext  = FilenameUtils.getExtension(ko) 
                            name = FilenameUtils.removeExtension(ko)
                            sh "./pthConversion.sh ${io} ${jo} ${name} ${ext}" } //not incl. stage (4 parameters)
                    def nexus_creds = [
                    [path: 'secrets/creds/nexus', secretValues: [
                    [envVar: 'nexus_pwd', vaultKey: 'password']]]]
                    wrap([$class: 'VaultBuildWrapper', vaultSecrets: nexus_creds]) {
                    sh "./pthUpload.sh ${io} ${jo}" }
                    break //PTH stage finished
                    default: println 'TBD'
                } //switch .PTH/.ATH/ 
            } //loop lvl2 
        } //loop lvl1 [MNR19]
    } //dir ()
}//end
