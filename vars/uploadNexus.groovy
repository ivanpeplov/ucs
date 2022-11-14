def call(String path) {
    dir (path) {
        def nexus_creds = [
        [path: 'secrets/creds/nexus', secretValues: [
        [envVar: 'nexus_pwd', vaultKey: 'password']]]]
        //level 1 - group folder
        lvl1 = listDir(path)
        lvl2=[]
        //to delete ./svn folder recursively
        sh "find . -type d -name .svn -exec rm -rf {} +"
        //level 2 - project folder
        for (int i = 0; i < lvl1.size(); i++) {
        lvl2[i] = listDir("${path}/${lvl1[i]}/")
            for (int j=0; j < lvl2[i].size(); j++) {
                //level 3 - target folder: workspace/svn_path/lvl1/lvl2/LVL3
                lvl3=listDir("${path}/${lvl1[i]}/${lvl2[i][j]}")
                for (int k=0; k < lvl3.size(); k++) {
                    wrap([$class: 'VaultBuildWrapper', vaultSecrets: nexus_creds]) {
                    sh """
                    pushd ${lvl1[i]}/${lvl2[i][j]}/${lvl3[k]}
                    if [ -z "\$(ls -A )" ]; then echo "Empty"
                    else
                    zip -r -q ${lvl3[k]}.zip *
                    curl -s -u admin:'${nexus_pwd}' --upload-file ${lvl3[k]}.zip ${nexus_url}/${SVN_PATH}/${lvl1[i]}/${lvl2[i][j]}/
                    fi
                    """
                    } // vault wrapper
                } //lvl3 loop
            } //lvl2 loop
        } //lvl1 loop
    } // dir ()
}//end of def call()
