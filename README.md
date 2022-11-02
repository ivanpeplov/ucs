# cpp
FIS server C++ Jenkins declarative pipeline
Полное название проекта: UCS/fis
SVN проект FIS
node LEGACY - jenkins-legacy - ip=172.16.10.235
node ROSA   - jenkins-rosa   - ip=172.16.10.236
Исходники:
Trunk:
https://svn/scm/svn/dev/FIS/new/trunk/units
Tags: 
https://svn/scm/svn/dev/FIS/new/tags/2019AU/units
https://svn/scm/svn/dev/FIS/new/tags/2020KA/units - PROD

Артифакты: http://10.255.252.161:8081/service/rest/repository/browse/prime-artifacts-local/dev/
LIBs: baselib, libfis, mqlib
BINARIES: fis/samples (deps from baselib and fis); 
#
FIS server C++ Jenkins declarative pipeline - утилиты
Полное название проекта: UCS/fis
SVN проект FIS
node LEGACY - jenkins-legacy - ip=172.16.10.235
node ROSA   - jenkins-rosa   - ip=172.16.10.236
Исходники:
Trunk:
https://svn/scm/svn/dev/FIS/new/trunk/opcpposcv (PutFileToQueue/GetFileFromQueue)
Tags: 
https://svn/scm/svn/dev/FIS/new/tags/2019AU/opcpposcv (PutFileToQueue/GetFileFromQueue)
https://svn/scm/svn/dev/FIS/new/tags/2020KAopcpposcv  (PutFileToQueue/GetFileFromQueue) - PROD

Артифакты: http://10.255.252.161:8081/service/rest/repository/browse/prime-artifacts-local/dev/
BINARIES:opcpposcv, PutFileToQueue/GetFileFromQueue
#
Сборка функциональных модулей для HSM
Полное название проекта: UCS/gemalto; UCS/eracom
SVN проект FM
node GEM - jenkins-gem - ip=172.16.64.70
node ERA - eracom      - ip=172.24.31.199 (win7 32x)
Исходники: https://svn/scm/svn/dev/PassKey/FM/FmUX
Артифакты: http://10.255.252.161:8081/service/rest/repository/browse/prime-artifacts-local/dev/
BINARIES: obj-ppcfm/FmUX.bin - GEMALTO; obj-armfm/FmUX.bin - ERACOM
#
Сборка функциональных модулей для TID Manager (Borland C++ Builder 6.0)
Полное название проекта: UCS/tid_man
SVN проект CardPro
node BORLAND - jenkins-borland - ip=10.255.250.10 (win10 32x)
Исходники: https://svn/scm/svn/dev/CardPro/TidManager/TID_v6
Артифакты: http://10.255.252.161:8081/service/rest/repository/browse/prime-artifacts-local/dev/
BINARIES: .dll, exe, 
nexus repo for artifacts 10.255.252.161
gitlab: http://10.255.252.160/prime/cpp
#
Перекладка скриптов из svn в nexus
Полное название проекта: UCS/svn_nexus
SVN проект NexusShareAsIs
node ROSA   - jenkins-rosa   - ip=172.16.10.236
Исходники: https://svn/scm/svn/dev/NexusShareAsIs
Артифакты: http://10.255.252.161:8081/service/rest/repository/browse/prime-artifacts-local/dev/
BINARIES: 
nexus repo for artifacts 10.255.252.161
gitlab: http://10.255.252.160/prime/cpp
#
shared-library in gitlab: /vars
scriptler scripts: /var/lib/jenkins/.jenkins/scriptler/scripts