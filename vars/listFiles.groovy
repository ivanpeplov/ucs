import org.apache.commons.io.FilenameUtils
def call(String dir) {
    def osName = isUnix() ? "UNIX":"WINDOWS"
    if(isUnix()) {
        def fileList = []
        //dont use 'ls -la'. 'a' option adds dot folders in output: . and ..
        def output = sh returnStdout: true, script: "ls -l ${WORKSPACE}/${dir} | grep -v '^d' | awk '{a=match(\$0, \$9); print substr(\$0,a)}' | tail -n +2"
        fileList = output.tokenize('\n').collect() { it }
        for (i in 0..<fileList.size()) {
        fileList[i] = FilenameUtils.removeExtension(fileList[i])
        }
        return fileList
    } else {
        echo "TBD"
        /*proc = bat (returnStdout: true, script: "dir /A:D /B")
        def removeList = proc.split().toList()
        //clear junk elements
        removeList.subList(0, 3).clear()
        return removeList*/
    }
}