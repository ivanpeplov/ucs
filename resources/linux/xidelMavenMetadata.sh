#!/bin/bash
xquery='''
for $x in doc("maven-metadata.xml")/metadata/versioning/versions
return $x/version'''
/var/lib/jenkins/xidel http://10.255.252.161:8081/repository/ucs_repo/ru/ucs/mmcore/maven-metadata.xml --xquery  "$xquery" 