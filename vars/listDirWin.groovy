def call(String dir) {
    output = bat (returnStdout: true, script: "dir /A:D /B")
    dirList = output.split().toList()
    //clear junk elements
    dirList.subList(0, 3).clear()
    return dirList
}