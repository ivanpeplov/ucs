import org.apache.commons.io.FilenameUtils
def call(String dir) { //get filename.ext list from current dir
    full_dir="${WORKSPACE}/${dir}"
    proc = bat (returnStdout: true, script: "dir ${full_dir} /b /o:gn /A-D")
    fileList = output.split().toList()
    return fileList
    
}