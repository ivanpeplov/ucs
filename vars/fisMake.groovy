def call(String operation) {
    dir (operation) {
    units = MODULES.split(',').toList()
    units.each {f -> sh "cd ${f} ; echo ${CLEAR} ${RELEASE} | xargs -n 1 Make"}
    }
}
