def call(String name) {
    switch (name) {
        case "fis":
            //local preliminary file operations
            sh  "cp -R ~/orpo/fis/{lib,bin,tools}/ ${WORKSPACE}/"
            sh  "cd bin ; mkdir fis.bin"
            break
        case "fis_util":
            //local preliminary file operations
            sh  "cp -R ~/orpo/fis/{lib,bin,tools}/ ${WORKSPACE}/"
            break
        case "micro_mod":
            //local preliminary file operations
            loadLinuxScript('prepFilesMmod.sh')
            sh "./prepFilesMmod.sh"
            break
        case ["tid_man", "mms_eod", "palmera"] :
            //local preliminary file operations
            def workspace = WORKSPACE.replaceAll('/','\\\\')
            //bat "xcopy C:\\jenkins\\trunk ${workspace} /i /q /d /e"
            bat "xcopy ${workspace}\\${SVN}\\${VERSION} ${workspace} /i /q /d /e"
            bat "mkdir ${TARGET}"
            break
        default:
            println ("Default: fm, svn_nexus, etl_nexus")
    }
}
