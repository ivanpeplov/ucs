def call (String label, String bmp) {
    bat "make -f ${label}.mak & xcopy ${label}.exe ${TARGET}"
    bmp.split(',').each { filename -> bat "xcopy ${filename} ${TARGET}"}
}