import org.apache.commons.io.FilenameUtils
def call(String dir) { //get filename.ext list from current dir
    def fileList = [] // | sed -n '/.k[j-tb-r:]/p' --> select files with .ktr/.kjb extension
    def output = sh returnStdout: true, script: "ls -l ${dir} | grep -v '^d' | awk '{a=match(\$0, \$9); print substr(\$0,a)}' | tail -n +2"
    fileList = output.tokenize('\n').collect() { it }
    return fileList
}