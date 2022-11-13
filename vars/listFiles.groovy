import org.apache.commons.io.FilenameUtils
def call(String dir, String ext) { //get filename.ext list from current dir
    def osName = isUnix() ? "UNIX":"WINDOWS"
    if(isUnix()) {
        def fileList = []
        //dont use 'ls -la'. 'a' option adds dot folders in output: . and ..
        switch (ext) {
        case ("ktr") : // sed -n '/.k[j-tb-r:]/p' --> select files with .ktr/.kjb extension
        def output = sh returnStdout: true, script: "ls -l ${dir} | grep -v '^d' | awk '{a=match(\$0, \$9); print substr(\$0,a)}' | tail -n +2 | sed -n '/.k[j-tb-r:]/p'"
        fileList = output.tokenize('\n').collect() { it }
        break
        default :
        echo "default"
        def output = sh returnStdout: true, script: "ls -l ${dir} | grep -v '^d' | awk '{a=match(\$0, \$9); print substr(\$0,a)}' | tail -n +2"
        fileList = output.tokenize('\n').collect() { it }
        }
        //for (i in 0..<fileList.size()) { //moved at level above - pentahoETL.groovy
        //fileList[i] = FilenameUtils.removeExtension(fileList[i])}
        return fileList
    } else {
        def removeList = []
        proc = bat (returnStdout: true, script: "dir /b /o:gn /A-D")
        removeList = proc.split().toList()
        //for (i in 0..<removeList.size()) {
        //removeList[i] = FilenameUtils.removeExtension(removeList[i])}
        return removeList
    }
}