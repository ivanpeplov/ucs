def call (String label, String bmp) {
    bat "make -f ${label}.mak & xcopy ${label}.exe ${TARGET}"
    bmp.split(',').each { f -> bat "xcopy ${f} ${TARGET}"}
}