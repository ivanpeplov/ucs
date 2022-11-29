def call(String dir) {
    proc = bat (returnStdout: true, script: "dir /A:D /B")
    def removeList = proc.split().toList()
    //clear junk elements
    removeList.subList(0, 3).clear()
    return removeList
}