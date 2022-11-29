def call() {
//download /units folder from SVN
def osName = isUnix() ? "UNIX":"WINDOWS"
echo "osName: " + osName
    def svn_creds = [
    [path: 'secrets/creds/svn', secretValues: [
    [envVar: 'svn_pwd', vaultKey: 'password']]]]
    wrap([$class: 'VaultBuildWrapper', vaultSecrets: svn_creds]) {
        if(isUnix()) {
          loadLinuxScript('getNixSVN.sh')
          sh "./getNixSVN.sh"
        } else {
          loadWinBat('getWinSVN.bat')
          bat "getWinSVN.bat"
        }
    }
}
