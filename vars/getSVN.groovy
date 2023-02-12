def call() { //base method for SVN downloading
def osName = isUnix() ? "UNIX":"WINDOWS" // calling elvis
echo "osName: " + osName
    def svn_creds = [
    [path: 'secrets/creds/svn', secretValues: [
    [envVar: 'svn_pwd', vaultKey: 'password']]]]
    wrap([$class: 'VaultBuildWrapper', vaultSecrets: svn_creds]) {
        if(isUnix()) {
          loadScript(place:'linux', name:'getSVN.sh')
          sh "./getSVN.sh"
        } else {
          loadScript(place:'win', name:'getSVN.bat')
          bat "getSVN.bat"
        }
    }
}
