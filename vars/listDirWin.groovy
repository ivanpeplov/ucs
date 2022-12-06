def call() {
    output = bat (returnStdout: true, script: "dir /A:D /B")
    dirList = output.split().toList()
    dirList.subList(0, 3).clear()
    return dirList
}