def call (String lvl1, String exe) {
def nexus_creds = [
[path: 'secrets/creds/nexus', secretValues: [
[envVar: 'nexus_pwd', vaultKey: 'password']]]]
    wrap([$class: 'VaultBuildWrapper', vaultSecrets: nexus_creds]) {
    sh "./pthUpload.sh ${lvl1} ${exe}" }
}