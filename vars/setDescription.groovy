def call() {
//build description inside web UI
    switch (JOB_BASE_NAME) {
    case ["fis", "borland"]:
    currentBuild.description = "node: ${NODE_NAME}\n label: ${LABEL}; svn: ${SVN}; version: ${VERSION}\nbuild_number: ${BUILD_NUMBER}"
    break
    case ["mm_android", "fm_host"]:
    currentBuild.description = "node: ${NODE_NAME}\n label: ${LABEL}; build_number: ${BUILD_NUMBER}"
    break
    default:
    currentBuild.description = "node: ${NODE_NAME}\nbuild_number: ${BUILD_NUMBER}"
    }
}
