#!/bin/bash
xquery='''
for $x in doc("maven-metadata.xml")/metadata/versioning/versions
return $x/version'''
/var/lib/jenkins/xidel $NEXUS_MAVEN/ru/ucs/mmcore/maven-metadata.xml --xquery  "$xquery"
