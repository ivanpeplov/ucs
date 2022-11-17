def call(String path) {
    dir (path) {
        def nexus_creds = [
        [path: 'secrets/creds/nexus', secretValues: [
        [envVar: 'nexus_pwd', vaultKey: 'password']]]]
        //level 1 - group folder
        lvl1 = listDir("${WORKSPACE}/${path}")
        lvl2=[]
        //to delete ./svn folder recursively
        sh "find . -type d -name .svn -exec rm -rf {} +"
        loadLinuxScript('uploadNexus.sh') //bash for curl to Nexus
        //level 2 - project folder
        for (int i = 0; i < lvl1.size(); i++) {
        lvl2[i] = listDir("${WORKSPACE}/${path}/${lvl1[i]}")
            for (int j=0; j < lvl2[i].size(); j++) {
                //level 3 - target folder: workspace/svn_path/lvl1/lvl2/LVL3
                lvl3=listDir("${WORKSPACE}/${path}/${lvl1[i]}/${lvl2[i][j]}")
                for (int k=0; k < lvl3.size(); k++) {
                    wrap([$class: 'VaultBuildWrapper', vaultSecrets: nexus_creds]) {
                    sh "./uploadNexus.sh ${lvl1[i]} ${lvl2[i][j]} ${lvl3[k]}"
                    } // vault wrapper
                } //lvl3 loop
            } //lvl2 loop
        } //lvl1 loop
    } // dir ()
}//end
