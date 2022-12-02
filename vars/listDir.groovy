def call(String dir) {
    full_dir="${WORKSPACE}/${dir}"
    def foldersList = []
    def output = sh returnStdout: true, script: "ls -l ${full_dir} | grep ^d | awk '{print \$9}'"
    //folderlist.text.eachLine {files.add(it)} 
    foldersList = output.tokenize('\n').collect() { it }
    return foldersList
}