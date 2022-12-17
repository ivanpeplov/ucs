#!/bin/bash
#used at jenkins-master only
xquery='''
for $x in doc("maven-metadata.xml")/metadata/versioning/versions
return $x/version'''
/var/lib/jenkins/bin/xidel $NEXUS_MAVEN_ORPO/ru/ucscards/mmcore/maven-metadata.xml --xquery  "$xquery"
