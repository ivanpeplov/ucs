def Nix(String dir) { //for all jenkinsfile
    full_path="${WORKSPACE}/${dir}"
    output = sh returnStdout: true, script: "ls -l ${full_path} | grep ^d | awk '{print \$9}'"
    dirList = output.split().toList()
    return dirList
}
def Win(String dir) {
    output = bat (returnStdout: true, script: "dir /A:D /B")
    dirList = output.split().toList()
    dirList.subList(0, 3).clear()
    return dirList
}