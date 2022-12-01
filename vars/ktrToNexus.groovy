def call(String path) { //v2.0 01.12.2022
    dir (path) {
        loadScript(place:'linux', name:'ktrUpload.sh') //bash for curl to Nexus
        def nexus_creds = [
        [path: 'secrets/creds/nexus', secretValues: [
        [envVar: 'nexus_pwd', vaultKey: 'password']]]]
        sh "find . -type d -name .svn -exec rm -rf {} +" //to delete ./svn folder recursively
        lvl1 = listDirNix("${WORKSPACE}/${path}")
        for (xo in lvl1) { lvl2=listDirNix("${WORKSPACE}/${path}/${xo}")
            for (yo in lvl2) { lvl3=listDirNix("${WORKSPACE}/${path}/${xo}/${yo}")
                for (zo in lvl3) {
                    wrap([$class: 'VaultBuildWrapper', vaultSecrets: nexus_creds]) {
                    sh "./ktrUpload.sh ${xo} ${yo} ${zo}" } //vault wrapper    
                }
            }
        }
    } // dir()
}//end
