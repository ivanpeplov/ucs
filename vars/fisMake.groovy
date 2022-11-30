def call(String operation) {
    dir (operation) {
    //List after ACTIVE CHOICE properties is string divided ','. Need a split() to list []
    unitList = MODULES.split(',').toList()
    unitList.each {filename -> sh "pushd ${filename} ; echo ${CLEAR} ${RELEASE} | xargs -n 1 ${PROJECTS}/tools/Make"}
    }
}
