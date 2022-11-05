def call() {
    try { if (MODULES == '') {
            println('Please select one Utility checkbox')
            error('autoStop') } }
        catch(e) { if (e.message == 'autoStop') {
            currentBuild.result = 'ABORTED' }
        throw e }
}
