def call(String operation) {
    dir (operation) {
    //List after ACTIVE CHOICE properties is string divided ','. Need a split()
    mod = MODULES.split(',').toList()
    dirList = mod
        for (int i=0; i < dirList.size(); i++) {
        sh "pushd ${dirList[i]} ; echo ${CLEAR} ${RELEASE} | xargs -n 1 ${PROJECTS}/tools/Make"
        }
    }
}
