def call (String lvl1, String exe) {
def nexus_creds = [
[path: 'secrets/creds/nexus', secretValues: [
[envVar: 'nexus_pwd', vaultKey: 'password']]]]
    wrap([$class: 'VaultBuildWrapper', vaultSecrets: nexus_creds]) {
    sh """
    pushd ${lvl1}; zip -q -u  ${exe}.zip ktr_xml.log ; zip -q -u -j ${exe}.zip ./BIN/CheckSql.log;
    curl -s -u admin:'${nexus_pwd}' --upload-file ${exe}.zip ${NEXUS_URL_TEST}/${SVN_PATH}/${lvl1}/; 
    rm ${exe}.zip; rm *.log; rm ./BIN/*.log
    """
    }
}