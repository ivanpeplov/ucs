def call(String name) {
    def workspace = WORKSPACE.replaceAll('/','\\\\')
    switch (name) {//local preliminary file operations
        case ["fis", "fis_util", "mm_nix"] :
            loadLinuxScript('prepareFiles.sh')
            sh "./prepareFiles.sh"
            break
        case ["tid_man", "mms_eod", "palmera"] :
            //bat "xcopy C:\\jenkins\\trunk ${workspace} /i /q /d /e"
            bat "xcopy ${workspace}\\${SVN}\\${VERSION} ${workspace} /i /q /d /e"
            bat "mkdir ${TARGET}"
            break
        case ["mm_win"] :
            //bat "xcopy ${workspace}\\MicroModule ${workspace} /i /q /d /e"
            bat "copy ${workspace}\\git\\config\\sln_proj.zip ${workspace}"
            bat "7z x -y sln_proj.zip" //temp step, extract .sln/.vcxproj for build
        default:
            println ("Default: fm, svn_nexus, etl_nexus")
    }
}
