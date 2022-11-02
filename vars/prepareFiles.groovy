def call(String name) {
    switch (name) {
        case "fis":
            //local preliminary file operations
            sh  "cp -R ~/projects/{lib,bin,tools}/ ${WORKSPACE}/"
            sh  "mkdir ${WORKSPACE}/bin/fis.bin"
            break
        case "utility_fis":
            //local preliminary file operations
            sh  "cp -R ~/projects/{lib,bin,tools}/ ${WORKSPACE}/"
            break
        case ["tid_man", "mms_eod"] :
            //local preliminary file operations
            def workspace = WORKSPACE.replaceAll('/','\\\\')
            //temp xcopy before getSVN is available. CURRENT - mmseod
            //bat "xcopy C:\\jenkins\\mmseod ${workspace} /i /q /d /e"
            //
            bat "xcopy ${workspace}\\${SVN}\\${VERSION} ${workspace} /i /q /d /e"
            bat "mkdir ${TARGET}"
            break
        default:
            println ("FM GEMALTO")
    }
}
