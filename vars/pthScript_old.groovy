import org.apache.commons.io.FilenameUtils
def call(String path) { //v4.0 15.12.2022
    dir (path) { //path="TestSQLtoNexus"
        def nexus_creds = [ //masking nexus credentials
            [ path: 'secrets/creds/nexus', secretValues: [
            [ envVar: 'nexus_pwd', vaultKey: 'password']]]]
        sh "find . -type d -name .svn -exec rm -rf {} +" //to delete junk /.svn folder recursively at lvl1
        loadScript(place:'linux', name:'spaceToUnderscore.sh')
        sh "./spaceToUnderscore.sh" //change " " to "_" in filenames recursively
        loadScript(place:'linux', name:'pthUpload_old.sh') // bash
        loadScript(place:'linux', name:'pthConversion_old.sh') // ktr2xml + checkerSQL
        loadScript(place:'linux', name:'checkerSQL_old.sh') // sql checker
        //loadScript(place:'linux', name:'xsltcPTH.sh') // ktr2xml converter
        lvl1 = listDir("${path}") //level 1 - release folder [MNR19]
        for (io in lvl1) { lvl2 = listDir("${path}/${io}")
            lvl2 = lvl2 - 'BIN' //[AMSBatch.PTH, BonusETL.PTH, ..., NTPREFS.XDB] - 'BIN'
            for (jo in lvl2) {
              ext = FilenameUtils.getExtension(jo) // [PTH, PTH, ..., XDB]
              stage = listDir("${path}/${io}/${jo}") 
              if (stage != '') { for (lo in stage) { // if has folders - recursively process them
              pthConversion_old (todo:"${ext}", r:"${path}", l1:"${io}", l2:"${jo}", ss:"${lo}") } } /*substage recursion*/
              //stage conversion
              pthConversion_old (todo:"${ext}", r:"${path}", l1:"${io}", l2:"${jo}") /*stage no recursion*/ 
              wrap([$class: 'VaultBuildWrapper', vaultSecrets: nexus_creds]) 
              { sh "./pthUpload_old.sh ${io} ${jo}" } // upload to nexus
            } //loop lvl2 
        } //loop lvl1
    } //dir()
}//end