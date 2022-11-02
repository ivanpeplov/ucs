def call(String operation) {
    node (NODE) {
        dir (operation) {
        //List after ACTIVE CHOICE properties is string divided ','. Need a split()
        mod = MODULES.split(',').toList()
        dirList = mod
            for (int i=0; i < dirList.size(); i++) {
            switch (RELEASE) {
                case 'release':
                sh "pushd ${dirList[i]} ; echo rclear ${RELEASE} | xargs -n 1 ${PROJECTS}/tools/prjMake"
                break
                default:
                sh "pushd ${dirList[i]} ; echo dclear ${RELEASE} | xargs -n 1 ${PROJECTS}/tools/prjMake"
                }
            }
        }
    }
}
