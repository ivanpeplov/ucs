def call(String dir) {
    full_dir="${WORKSPACE}/${dir}"
    def foldersList = []
    def output = sh returnStdout: true, script: "ls -l ${full_dir} | grep ^d | awk '{print \$9}'"
    //folderlist.text.eachLine {files.add(it)} 
    foldersList = output.tokenize('\n').collect() { it }
    return foldersList
}
    //when using map [:] as parameter
    /*def call(Map dir=[:]) {
    dir.get('l1', '') //default value for map. change 'null' to ''
    dir.get('l2', '') //default value for map. change 'null' to ''
    def output = sh returnStdout: true, script: "./listDir.sh ${dir.root} ${dir.l1} ${dir.l2}" */