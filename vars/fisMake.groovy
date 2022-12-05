def call(String operation) {
    dir (operation) {
    //List after ACTIVE CHOICE properties is string divided ','. Need a split() to list []
    units = MODULES.split(',').toList()
    units.each {f -> sh "pushd ${f} ; echo ${CLEAR} ${RELEASE} | xargs -n 1 ${PROJECTS}/tools/Make"}
    }
}
