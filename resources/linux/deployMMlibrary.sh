#!/bin/bash
gradle -DARG=${VERSION} downloadFile -b tools_lib.gradle
touch local.properties & echo 'sdk.dir = /home/jenkins/android' >> local.properties
gradle build -b build_lib.gradle
gradle publish -b tools_lib.gradle
