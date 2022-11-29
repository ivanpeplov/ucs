def call() {
//build description inside web UI
    switch (JOB_BASE_NAME) {
    case ["fis", "fis_util", "tid_man", "mms_eod", "palmera"]:
    currentBuild.description = "node: ${NODE_NAME}\nsvn: ${SVN}; version: ${VERSION}\nbuild_number: ${BUILD_NUMBER}"
    break
    case ["borland"]:
    currentBuild.description = "node: ${NODE_NAME}\n label: ${LABEL}; svn: ${SVN}; version: ${VERSION}\nbuild_number: ${BUILD_NUMBER}"
    break
    default:
    currentBuild.description = "node: ${NODE_NAME}\nbuild_number: ${BUILD_NUMBER}"
    }
}
