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
        //level 2 - define conversion dirs
        for (int i = 0; i < lvl1.size(); i++) {
        println lvl1[i] // BNR15
        lvl2[i] = listDir("${path}/${lvl1[i]}/")
        println lvl2[i] //[AMSBatch.PTH, BIN, BMSBatch.PTH, CMSBatch.ATH]
        bin=['BIN']
        exe[i]=lvl2[i] - bin
        println exe[i] //[AMSBatch.PTH, BMSBatch.PTH, CMSBatch.ATH] - [BIN]
            for (int j=0; j < exe[i].size(); j++) {
            //Get folder extension. PTH from AMSBatch.PTH
            ext[j] = FilenameUtils.getExtension(exe[i][j])
                // switch for different conversion scripts. currently active: PTH (.ktr to .xml)
                switch ("${ext[j]}") {
                    case ('PTH'): //PTH conversion for Pentaho
                    println "${ext[j]}"
                    ktr = listFiles("${ROOT}/${lvl1[i]}/${exe[i][j]}")
                        for (int k=0; k < ktr.size(); k++) {
                        println "${ktr[k]}" //each .ktp script
                        sh """
                        # set +e - for testing only. In prod - comment it
                        set +e
                        pushd ${lvl1[i]}; java -jar ./BIN/xsltc.jar -i "./${exe[i][j]}/${ktr[k]}.ktr" -o "./${exe[i][j]}/${ktr[k]}.xml" -l stdout.log -x ./BIN/pth2lst.xslt
                        # conversion log
                        cat stdout.log >> ktr_xml.log
                        # verification log
                        pushd BIN; echo "---------- ${ktr[k]}.xml ----------" >> CheckSql.log; java -jar checkersql.jar "../${exe[i][j]}/${ktr[k]}.xml"
                        popd +0; zip -q -u ${exe[i][j]}.zip ./${exe[i][j]}/*.xml
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
                    pushd ${lvl1[i]}; zip -q -u  ${exe[i][j]}.zip ktr_xml.log ; zip -q -u -j ${exe[i][j]}.zip ./BIN/CheckSql.log;
                    curl -s -u admin:'${nexus_pwd}' --upload-file ${exe[i][j]}.zip ${NEXUS_URL_TEST}/${SVN_PATH}/${lvl1[i]}/; 
                    rm ${exe[i][j]}.zip; rm *.log; rm ./BIN/*.log
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
