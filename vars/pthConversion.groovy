def call (String lvl1, String exe, String stage, String name, String ext) {
    sh """
    # set +e - for testing only. In prod - comment it
    set +e
    pushd ${lvl1}; java -jar BIN/xsltc.jar -i "${exe}/${stage}/${name}.${ext}" -o "${exe}/${stage}/${name}.xml" -l stdout.log -x BIN/pth2lst.xslt
    cat stdout.log >> ktr_xml.log
    pushd BIN; echo "---------- ${name}.xml ----------" >> CheckSql.log;
    java -jar checkersql.jar "../${exe}/${stage}/${name}.xml"
    if [ -z "${stage}" ]; then 
      popd +0; zip -q -u ${exe}.zip ./${exe}/*.xml
    else
      popd +0; zip -q -u ${exe}.zip ./${exe}/${stage}/*.xml
    fi
    # true - for testing only. In prod - comment it
    true
    """
}