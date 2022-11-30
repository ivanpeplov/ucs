def call() {
//download /units folder from SVN
def osName = isUnix() ? "UNIX":"WINDOWS"
echo "osName: " + osName
    def svn_creds = [
    [path: 'secrets/creds/svn', secretValues: [
    [envVar: 'svn_pwd', vaultKey: 'password']]]]
    wrap([$class: 'VaultBuildWrapper', vaultSecrets: svn_creds]) {
        if(isUnix()) {
          loadScript(place:'linux', name:'getNixSVN.sh')
          sh "./getNixSVN.sh"
        } else {
          loadScript(place:'win', name:'getWinSVN.bat')
          bat "getWinSVN.bat"
        }
    }
}
