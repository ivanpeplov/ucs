def call (String lvl1, String exe, String stage, String name, String ext) {
    sh "./pthConversion.sh ${lvl1} ${exe} \"${stage}\" ${name} ${ext}"
}