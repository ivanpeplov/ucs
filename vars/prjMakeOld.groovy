def call(String operation) {
    node (NODE) {
        dir (operation) {
            switch (RELEASE) {
                case 'release':
                    sh "echo rclear ${RELEASE} | xargs -n 1 ${PROJECTS}/tools/prjMake"
                break
                default:
                    sh "echo dclear ${RELEASE} | xargs -n 1 ${PROJECTS}/tools/prjMake"
            }
        }
    }
}
