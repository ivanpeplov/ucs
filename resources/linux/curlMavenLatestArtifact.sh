#!/bin/bash
#get artifact from nexus-3 maven2 repo with latest version
curl -L -X GET "http://10.255.250.50:8081/service/rest/v1/search/assets/download?sort=version&repository=ucs_lib&maven.groupId=ru.ucs.micromod&maven.artifactId=mmcore&maven.extension=jar" -H "accept: application/json" --output mmcore.jar