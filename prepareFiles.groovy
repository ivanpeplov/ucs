def call(String name) {
    switch (name) {
        case "fis":
            //local preliminary file operations
            sh  "cp -R ~/projects/{lib,bin,tools}/ ${WORKSPACE}/"
            sh  "cd bin ; mkdir fis.bin"
            break
        case "micro_mod":
            //local preliminary file operations
            sh  "cp -R ~/micromod/{lib,bin,tools,units}/ ${WORKSPACE}/"
            modules=['cyassl-3.2.0', 'myizip_z', 'microx_t', 'axcoder']
            modules.each { filename -> sh "cp -r ${WORKSPACE}/${TOOR}/${filename}/ ${PROJECTS}/units"}
            sh """
            cp ${PROJECTS}/units/axcoder/axorlib.* ${PROJECTS}/units/microx_t/samples/ucs_mm/sources/
            cp ${PROJECTS}/units/microx_t/sources/SLogger.cpp ${PROJECTS}/units/microx_t/samples/ucs_mm/sources/
            cp ${PROJECTS}/units/microx_t/sources/SLogger.cpp ${PROJECTS}/units/microx_t/samples/ucs_ms/sources/
            # temp cp for PadsWork
            cp ${PROJECTS}/tools/PadsWork.hpp ${PROJECTS}/units/microx_t/samples/ucs_mm/include/
            """
            break
        
        case "fis_util":
            //local preliminary file operations
            sh  "cp -R ~/projects/{lib,bin,tools}/ ${WORKSPACE}/"
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
