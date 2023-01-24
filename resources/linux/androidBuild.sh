#!/bin/bash
# mm_android.groovy
touch local.properties & echo 'sdk.dir = /home/jenkins/tools/android' >> local.properties
case "${LABEL}" in
mmcore) 
    sed -i '$d' pom.xml
    #attached after last line
    cat addToPom.xml >> pom.xml
    mvn deploy ;;
mmlibrary) 
    gradle -DARG=${VERSION} downloadFile -b tools_lib.gradle
    gradle -DMINSDK=${MINSDK} build -b build_lib.gradle
    gradle -DMINSDK=${MINSDK} publish -b tools_lib.gradle ;;
app)
    gradle build -b build.gradle ;;
evotor)
    gradle downloadFile -b build.gradle
    cp evotor_app.gradle ./app/build.gradle 
    gradle build ;;
esac

