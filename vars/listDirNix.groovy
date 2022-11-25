def call(String dir) {
    def foldersList = []
    //dont use 'ls -la'. 'a' option adds dot folders in output: . and ..
    def output = sh returnStdout: true, script: "ls -l ${dir} | grep ^d | awk '{print \$9}'"
    foldersList = output.tokenize('\n').collect() { it }
    return foldersList
}