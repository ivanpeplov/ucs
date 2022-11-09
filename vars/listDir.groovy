def call(String dir) {
    def osName = isUnix() ? "UNIX":"WINDOWS"
    if(isUnix()) {
        def foldersList = []
        //dont use 'ls -la'. 'a' option adds dot folders in output: . and ..
        def output = sh returnStdout: true, script: "ls -l ${WORKSPACE}/${dir} | grep ^d | awk '{print \$9}'"
        foldersList = output.tokenize('\n').collect() { it }
        return foldersList
    } else {
        proc = bat (returnStdout: true, script: "dir /A:D /B")
        def removeList = proc.split().toList()
        //clear junk elements
        removeList.subList(0, 3).clear()
        return removeList
    }
}