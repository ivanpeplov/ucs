def call(String path) { //chk_sql.groovy
    dir (path) { //path="MNR19"
        def nexus_creds = [ //masking nexus credentials
            [ path: 'secrets/creds/nexus', secretValues: [
            [ envVar: 'nexus_pwd', vaultKey: 'password']]]]
        sciptList=list.split(',') //.sh scripts list from .yml file
        sciptList.each {s -> loadScript(place:'linux', name: "${s}")}
        sh "./spaceToUnderscore.sh; find . -type d -name .svn -exec rm -rf {} + "
        lvl2 = listDir.Nix("${path}") - 'BIN' //[AMSBatch.PTH, BonusETL.PTH, ..., NTPREFS.XDB] - 'BIN'
        for (itLvl2 in lvl2) {
            exe = itLvl2.substring (itLvl2.indexOf(".") +1) // [PTH, PTH, ..., XDB]
            lvl3 = listDir.Nix ("${path}/${itLvl2}") 
            if (lvl3 != '') {
                for (itLvl3 in lvl3) // if has folders - recursively process them
                { pthConversion (todo:"${exe}", l1:"${path}", l2:"${itLvl2}", l3:"${itLvl3}") } // lvl3 recursion
            } //lvl3 conversion
            pthConversion (todo:"${exe}", l1:"${path}", l2:"${itLvl2}") // no lvl3 recursion
            wrap([$class: 'VaultBuildWrapper', vaultSecrets: nexus_creds]) 
            { sh "./pthUpload.sh ${itLvl2}" } // upload to nexus
        }
    }
}