def call(String rt, String label) { //borland groovy
    dir (rt) {
        switch (label) {  //module selection
            case ["cardpro", "mmseod", "palmerauloade", "passkey"] :
            bat "make -f ${label}.mak & xcopy ${label}.exe ${TARGET}"
            bmp.split(',').each { f -> bat "xcopy ${f} ${TARGET}"}
            break
            case ["bin", "32"] : 
            bpl.split(',').each { f -> bat "xcopy ${f} ${TARGET}" }
            break
            case ("cardlib") : 
            bat "make -f cardlib.mak & xcopy C:\\bpl\\*.bpl ${TARGET}"
            break 
            case ("form") :
                dir = listDir.Win("form") - 'TEMPLATE'
                for (io in dir) {
                    if (io=='PRINT.CFG') { frm.split(',').each {f->bat "xcopy .\\${io}\\${f} ${TARGET}\\${rt}\\${io}\\"} }
                    else { bat "cd ${io} & make -f ${io}.mak & xcopy ${io}.dll ${TARGET}\\${rt}\\${io}\\" } }
            break
        }
    }
}