def call(String dir) {
    full_dir="${WORKSPACE}\\${dir}"
    proc = bat (returnStdout: true, script: "dir ${full_dir} /A:D /B")
    def removeList = proc.split().toList()
    //clear junk elements
    removeList.subList(0, 4).clear()
    return removeList
}