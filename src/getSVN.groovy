def call() {
//download /units folder from SVN
def osName = isUnix() ? "UNIX":"WINDOWS"
echo "osName: " + osName
    def svn_creds = [
    [path: 'secrets/creds/svn', secretValues: [
    [envVar: 'svn_pwd', vaultKey: 'password']]]]
    wrap([$class: 'VaultBuildWrapper', vaultSecrets: svn_creds]) {
        if(isUnix()) {
          sh "svn co --quiet --username jenkins --password "+'${svn_pwd}'+" https://${SVN_URL}/${SVN_PATH}"
        } else {
          bat "svn co --quiet --username jenkins --password=${svn_pwd} --non-interactive --trust-server-cert-failures=unknown-ca,cn-mismatch,expired,not-yet-valid,other https://${SVN_URL}/${SVN_PATH}"
        }
    }
}
