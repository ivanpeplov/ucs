def call(String path) {
dir (path) {
    def nexus_creds = [
        [path: 'secrets/creds/nexus', secretValues: [
            [envVar: 'nexus_pwd', vaultKey: 'password']]]]
      //level 1 - group folder
      lvl1 = listFiles(path)
    println lvl1
    lvl2=[]
    lvl1_lvl2=[]
    all=[]
        //to delete ./svn folder
        sh "rm -rf ./.svn"
        //level 2 - project folder
    for (int i = 0; i < lvl1.size(); i++) {
    lvl2[i] = listFiles("${path}/${lvl1[i]}/")
    println lvl2[i]
        for (int j=0; j < lvl2[i].size(); j++) {
        lvl1_lvl2[j] = "${WORKSPACE}/${path}/${lvl1[i]}/${lvl2[i][j]}"
        println lvl1_lvl2[j]
        //level 3 - target folder: workspace/svn_path/lvl1/lvl2/LVL3
        proc1 = sh (returnStdout: true, script: "rm -rf ./.svn ; ls ${lvl1_lvl2[j]}")
        lvl3 = proc1.split().toList()
        println lvl3 
            // all = workspace/svn_path/lvl1/lvl2/lvl3
            for (int k=0; k < lvl3.size(); k++) {
            all ="${lvl1_lvl2[j]}/${lvl3[k]}"
            println all
                //zip -j - remove junk path from archive
                wrap([$class: 'VaultBuildWrapper', vaultSecrets: nexus_creds]) {
                sh """
                pushd ${all}
                if [ -z "\$(ls -A )" ]; then echo "Empty"
                else
                zip -r -q ${lvl3[k]}.zip *
                curl -s -u admin:'${nexus_pwd}' --upload-file ${lvl3[k]}.zip ${nexus_url}/${SVN_PATH}/${lvl1[i]}/${lvl2[i][j]}/
                fi
               """
                }
            }
        }
    }
}
//
}//end of def call()
