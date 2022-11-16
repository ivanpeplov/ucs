def call(String operation) {
    dir (operation) {
    sh """
    chmod 750 configure
    ./configure --enable-opensslextra --enable-aesgcm --enable-sha512 --enable-ripemd --enable-ecc --enable-static
    make 2>errs
    cp ./src/.libs/libcyassl.a ${PROJECTS}/lib
    """
    }
}
