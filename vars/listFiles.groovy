def call(String dir) { //get filename.ext list from current dir
    full_dir="${WORKSPACE}/${dir}"
    def output = sh returnStdout: true, script: "ls -l ${full_dir} | grep -v '^d' | awk '{a=match(\$0, \$9); print substr(\$0,a)}' | tail -n +2"
    fileList = output.split().toList() //sed -n '/.k[j-tb-r:]/p' --> select files with .ktr/.kjb extension
    return fileList
}