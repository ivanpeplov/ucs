#!/bin/bash
# mm_java, mm_android
touch local.properties & echo 'sdk.dir = /home/jenkins/tools/android' >> local.properties
if [ "${LABEL}" = "mmcore" ]; then
#delete last line
sed -i '$d' pom.xml
#attached after last line
cat addToPom.xml >> pom.xml
mvn deploy
fi
if [ "${LABEL}" = "mmlibrary" ]; then
gradle -DARG=${VERSION} downloadFile -b tools_lib.gradle
gradle -DMINSDK=${MINSDK} build -b build_lib.gradle
gradle -DMINSDK=${MINSDK} publish -b tools_lib.gradle
fi
if [ "${LABEL}" = "sample" ]; then
gradle build -b sample.gradle
fi
if [ "${LABEL}" = "evotor" ]; then
gradle downloadFile -b evotor.gradle
cp evotor_app.gradle ./app/build.gradle 
cd app; gradle build -b build.gradle
fi
