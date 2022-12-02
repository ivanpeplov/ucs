def call(String path) { //v2.0 01.12.2022
    dir (path) {
        def nexus_creds = [
            [path: 'secrets/creds/nexus', secretValues: [
            [envVar: 'nexus_pwd', vaultKey: 'password']]]]
        sh "find . -type d -name .svn -exec rm -rf {} +" //to delete ./svn folder recursively
        loadScript(place:'linux', name:'ktrUpload.sh') //bash for curl to Nexus
        lvl1 = listDir("${path}")
        for (xo in lvl1) { lvl2=listDir("${path}/${xo}")
            for (yo in lvl2) { lvl3=listDir("${path}/${xo}/${yo}")
                for (zo in lvl3) {
                    wrap([$class: 'VaultBuildWrapper', vaultSecrets: nexus_creds]) {
                    sh "./ktrUpload.sh ${xo} ${yo} ${zo}" } //vault wrapper    
                }
            }
        }
    } // dir()
}//end
