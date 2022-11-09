import org.apache.commons.io.FilenameUtils
def call(String path) {
    dir (path) {
        def nexus_creds = [
        [path: 'secrets/creds/nexus', secretValues: [
        [envVar: 'nexus_pwd', vaultKey: 'password']]]]
        //level 1 - group folder
        lvl1 = listDir(path)
        println lvl1 //[BNR15, MNR19]
        lvl2=[]
        exe=[]
        ext=[]
        ktr=[]
        //to delete ./svn folder
        sh "rm -rf ./.svn"
        //level 2 - project folder
        for (int i = 0; i < lvl1.size(); i++) {
        println lvl1[i] // BNR15
        lvl2[i] = listDir("${path}/${lvl1[i]}/")
        println lvl2[i] //[AMSBatch.PTH, BIN, BMSBatch.PTH, CMSBatch.ATH]
        bin=['BIN']
        exe[i]=lvl2[i] - bin
        println exe[i] //[AMSBatch.PTH, BMSBatch.PTH, CMSBatch.ATH] - [BIN]
            for (int j=0; j < exe[i].size(); j++) {
            ext[j] = FilenameUtils.getExtension(exe[i][j])
                switch ("${ext[j]}") {
                    case ('PTH'):
                    println "${ext[j]}"
                    ktr = listFiles("${ROOT}/${lvl1[i]}/${exe[i][j]}")
                    for (int k=0; k < ktr.size(); k++) {
                    println "${ktr[k]}"
                    //PTH conversion for Pentaho
                    sh """ 
                    # set +e - for testing only. In prod - comment it
                    set +e
                    pushd ${lvl1[i]}; java -jar ./BIN/xsltc.jar -i "./${exe[i][j]}/${ktr[k]}.ktr" -o "./${exe[i][j]}/${ktr[k]}.xml" -l stdout.log -x ./BIN/pth2lst.xslt
                    pushd BIN; java -jar checkersql.jar "../${exe[i][j]}/${ktr[k]}.xml"
                    popd +0; zip -u -j ${exe[i][j]}.zip ./${exe[i][j]}/*.xml
                    # true - for testing only. In prod - comment it
                    true
                    """
                    //def sql = sh (returnStatus: true, script: "pushd ${lvl1[i]}/BIN; tail -n 1 CheckSql.log | awk '{print \$6}'")
                    //if (sql=='-1') {
                    //stopBuild('aborted')
                    //return
                    }
                     wrap([$class: 'VaultBuildWrapper', vaultSecrets: nexus_creds]) {
                    sh """
                    pushd ${lvl1[i]}; curl -s -u admin:'${nexus_pwd}' --upload-file ${exe[i][j]}.zip ${NEXUS_URL_TEST}/${SVN_PATH}/${lvl1[i]}/; rm ${exe[i][j]}.zip
                    """}
                    break
                    case ('RTH'): //future extensions; fake RTH
                    println 'to be define RTH method'
                    break
                    case ('ATH'): //future extensions; fake ATH
                    println 'to be define ATH method'
                    break
                    default:
                    println 'TBD'
                }
            }
        }        
    }
}//end of def call()
