def call() { //get filename.ext list from current dir
    proc = bat (returnStdout: true, script: "dir  /b /o:gn /A-D")
    fileList = output.split().toList()
    return fileList  
}