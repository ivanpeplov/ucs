import org.apache.commons.io.FilenameUtils
def call(String path) { //v3.0 03.12.2022
    dir (path) { //path="TestSQLtoNexus"
        def nexus_creds = [
            [path: 'secrets/creds/nexus', secretValues: [
            [envVar: 'nexus_pwd', vaultKey: 'password']]]]
        sh "find . -type d -name .svn -exec rm -rf {} +" //to delete junk /.svn folder recursively at lvl1
        lvl1 = listDir("${path}") //level 1 - group folder [MNR19]
        loadScript(place:'linux', name:'spaceToUnderscore.sh')
        sh "./spaceToUnderscore.sh" //change " " to "_" in filenames recursively
        loadScript(place:'linux', name:'pthUpload_c.sh') //bash script for upload .xml to Nexus
        loadScript(place:'linux', name:'pthConversion.sh') //bash script for PTH conversion
        for (io in lvl1) { lvl2 = listDir("${path}/${io}")
            lvl2=lvl2 - 'BIN' //[AMSBatch.PTH, BonusETL_top.PTH] - 'BIN'
            for (jo in lvl2) { ext = FilenameUtils.getExtension(jo)
                switch (ext) {
                case ('PTH') :
                    stage=listDir("${path}/${io}/${jo}")
                    if (stage != '') { //substage conversion
                        for (lo in stage) { pthConv (r:"${path}", l1:"${io}", l2:"${jo}", ss:"${lo}") } 
                    }
                        pthConv (r:"${path}", l1:"${io}", l2:"${jo}") //stage conversion
                    wrap([$class: 'VaultBuildWrapper', vaultSecrets: nexus_creds]) //upload to nexus
                    { sh "./pthUpload_c.sh ${io} ${jo}" }
                break //PTH stage finished
                default: println 'TBD'
                } //switch EXT finished
            } //loop lvl2 
        } //loop lvl1 [MNR19]
    } //dir ()
}//end
