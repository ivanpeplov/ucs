def call(String path) { //chk_sql.groovy
    dir (path) { //path="MNR19"
        loadScript(place:'linux', name:'spaceToUnderscore.sh')
        sh "./spaceToUnderscore.sh; find . -type d -name .svn -exec rm -rf {} + "
        loadScript(place:'linux', name:'pthChecker.sh') // ktr2xml + checkerSQL
        loadScript(place:'linux', name:'xdbChecker.sh') // only checkerSQL
        lvl2 = listDir.Nix("${path}") - 'BIN' //[AMSBatch.PTH, BonusETL.PTH, ..., NTPREFS.XDB] - 'BIN'
        for (jo in lvl2) {
            ext = jo.substring(jo.indexOf(".")+1); // [PTH, PTH, ..., XDB]
            stage = listDir.Nix("${path}/${jo}") 
            if (stage != '') { for (lo in stage) // if has folders - recursively process them
            { pthConversion (todo:"${ext}", l1:"${path}", l2:"${jo}", ss:"${lo}") } /*substage recursion*/
            } //stage conversion
            pthConversion (todo:"${ext}", l1:"${path}", l2:"${jo}") /*stage no recursion*/ 
            loadScript(place:'linux', name:'pthUpload.sh') // bash
						def nexus_creds = [ //masking nexus credentials
            [ path: 'secrets/creds/nexus', secretValues: [
            [ envVar: 'nexus_pwd', vaultKey: 'password']]]]
						wrap([$class: 'VaultBuildWrapper', vaultSecrets: nexus_creds]) 
            { sh "./pthUpload.sh ${jo}" } // upload to nexus
        }
    }
}