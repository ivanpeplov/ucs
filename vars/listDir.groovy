def call(String dir) {
    full_dir="${WORKSPACE}/${dir}"
    output = sh returnStdout: true, script: "ls -l ${full_dir} | grep ^d | awk '{print \$9}'"
    dirList = output.split().toList()
    return dirList
}