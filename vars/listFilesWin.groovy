import org.apache.commons.io.FilenameUtils
def call(String dir) { //get filename.ext list from current dir
    def removeList = []
    proc = bat (returnStdout: true, script: "dir /b /o:gn /A-D")
    removeList = proc.split().toList()
    //for (i in 0..<removeList.size()) {
    //removeList[i] = FilenameUtils.removeExtension(removeList[i])}
    return removeList
}