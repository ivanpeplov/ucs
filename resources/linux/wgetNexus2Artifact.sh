#!/bin/sh

#usage
# script.sh groupid artifactid version classifier type
# ex:
# script.sh log4j log4j 4.2.18 sources zip 
# script.sh log4j log4j 4.2.19  -> get jar as default
# script.sh log4j log4j 4.2.19-SNAPSHOT  -> get jar as default

repo="http://172.16.10.230:3389"
groupId=$1
artifactId=$2
version=$3

# optional
classifier=$4
type=$5

if [[ $type == "" ]]; then
  type="jar"
fi
if [[ $classifier != "" ]]; then
  classifier="-${classifier}"
fi

groupIdUrl="${groupId//.//}"
filename="${artifactId}-${version}${classifier}.${type}"

if [[ ${version} == *"SNAPSHOT"* ]]; then repo_type="snapshots"; else repo_type="releases"; fi

if [[ $repo_type == "releases" ]]
 then
   wget --no-check-certificate "${repo}/repository/releases/${groupIdUrl}/${artifactId}/${version}/${artifactId}-${version}${classifier}.${type}" -O ${filename} -k
 else
   versionTimestamped=$(wget -q -O- --no-check-certificate "${repo}/repository/snapshots/${groupIdUrl}/${artifactId}/${version}/maven-metadata.xml" | grep -m 1 \<value\> | sed -e 's/<value>\(.*\)<\/value>/\1/' | sed -e 's/ //g')

   wget --no-check-certificate "${repo}/repository/snapshots/${groupIdUrl}/${artifactId}/${version}/${artifactId}-${versionTimestamped}${classifier}.${type}" -O ${filename}
 fi
 
