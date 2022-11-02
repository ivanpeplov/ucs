def call() {
    try { if (VERSION == 'None' || VERSION == 'Select') {
            println('Please select BRANCH | VERSION selectors')
            error('autoStop') } }
        catch(e) { if (e.message == 'autoStop') {
            currentBuild.result = 'ABORTED' }
        throw e }
}
