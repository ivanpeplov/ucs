def call(String path) { //v5.0 27.12.2022
    dir (path) { //path="MNR19"
        def nexus_creds = [ //masking nexus credentials
            [ path: 'secrets/creds/nexus', secretValues: [
            [ envVar: 'nexus_pwd', vaultKey: 'password']]]]
        loadScript(place:'linux', name:'spaceToUnderscore.sh')
        sh "./spaceToUnderscore.sh; find . -type d -name .svn -exec rm -rf {} + "
        loadScript(place:'linux', name:'pthUpload.sh') // bash
        loadScript(place:'linux', name:'pthChecker.sh') // ktr2xml + checkerSQL
        loadScript(place:'linux', name:'xdbChecker.sh') // sql checker
        lvl2 = listDir("${path}") - 'BIN' //[AMSBatch.PTH, BonusETL.PTH, ..., NTPREFS.XDB] - 'BIN'
        for (jo in lvl2) {
            ext = jo.substring(jo.indexOf(".")+1); // [PTH, PTH, ..., XDB]
            stage = listDir("${path}/${jo}") 
            if (stage != '') { for (lo in stage) // if has folders - recursively process them
            { pthConversion (todo:"${ext}", l1:"${path}", l2:"${jo}", ss:"${lo}") } /*substage recursion*/
            } //stage conversion
            pthConversion (todo:"${ext}", l1:"${path}", l2:"${jo}") /*stage no recursion*/ 
            wrap([$class: 'VaultBuildWrapper', vaultSecrets: nexus_creds]) 
            { sh "./pthUpload.sh ${jo}" } // upload to nexus
        }
    }
}