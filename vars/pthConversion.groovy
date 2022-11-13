def call (String lvl1, String exe, String stage, String name, String ext) {
    sh """
    # set +e - for testing only. Clear pth.sh - 1min build time slower
    set +e
    pushd ${lvl1}; java -jar BIN/xsltc.jar -i "${exe}/${stage}/${name}.${ext}" -o "${exe}/${stage}/${name}.xml" -l stdout.log -x BIN/pth2lst.xslt
    cat stdout.log >> ktr_xml.log
    pushd BIN; echo "---------- ${name}.xml ----------" >> CheckSql.log;
    if [ -z "${stage}" ]; then
      java -jar checkersql.jar "../${exe}/${name}.xml"
      popd +0; zip -q -u ${exe}.zip ./${exe}/"${name}".xml
    else
      java -jar checkersql.jar "../${exe}/${stage}/${name}.xml"
      popd +0; zip -q -u ${exe}.zip ./${exe}/${stage}/"${name}".xml
    fi
    # true - for testing only. In prod - comment it
    true
    """
}