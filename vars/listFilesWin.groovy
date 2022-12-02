import org.apache.commons.io.FilenameUtils
def call(String dir) { //get filename.ext list from current dir
    full_dir="${WORKSPACE}/${dir}"
    proc = bat (returnStdout: true, script: "dir ${full_dir} /b /o:gn /A-D")
    removeList = proc.split().toList()
    return removeList
    //for (i in 0..<removeList.size()) {
    //removeList[i] = FilenameUtils.removeExtension(removeList[i])}
    
}