def call() {
    //build description inside web UI
    switch (JOB_BASE_NAME) {
            case ["fis", "utility_fis", "fisNew", "tid_man", "mms_eod"]:
                currentBuild.description = "node: ${NODE}\nsvn: ${SVN}; version: ${VERSION}\nbuild_number: ${BUILD_NUMBER}"
                break
            case ["gemalto", "eracom", "svn_nexus"]:
                currentBuild.description = "node: ${NODE}\nbuild_number: ${BUILD_NUMBER}"
                break
            default:
                println ("TBD")
        }
    
}
