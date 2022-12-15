def call() {
    //all actions for gradle build
    loadScript(place:'gradle', name:'build_core.gradle')
    loadScript(place:'linux', name:'getVersionFromSvnPom.sh')
    output = sh returnStdout: true, script: "./getVersionFromSvnPom.sh"
    versionFromPom = output.trim()
    sh "gradle -Pversion=${versionFromPom} build -b build_core.gradle"
    sh "gradle -Pversion=${versionFromPom} publish -b build_core.gradle"
}