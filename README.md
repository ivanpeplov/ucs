##
**FIS server**
FIS server C++ Jenkins declarative pipeline
JIRA: CI-51
SVN: FIS/new/
node: jenkins-legacy : ip=172.16.10.235
node: jenkins-rosa : ip=172.16.10.236
##
**Утилиты, относящиеся к проекту FIS**
JIRA: CI-51
SVN: FIS/new/
node: jenkins-legacy : ip=172.16.10.235
node: jenkins-rosa : ip=172.16.10.236
BINARIES: opcpposcv, GetFileFromQueue, PutFileToQueue
##
**Сборка функциональных модулей для HSM**
##
**сборка функц. модулей для HSM Eracom**
JIRA: CI-52
SVN: PassKey/FM/FmUX
node: eracom : ip=172.24.31.199 (Win7, x86)
BINARIES: obj-armfm/FmUX.bin
##
**сборка функц. модулей для HSM Gemalto**
JIRA: CI-52
SVN: PassKey/FM/FmUX
node: jenkins-gem : ip=172.16.64.70 (SUSE 12.5)
BINARIES: obj-ppcfm/FmUX.bin
##
***Сборка функциональных модулей (Borland C++ Builder 6.0)**
##
**Cборка TID Manager (Borland C++ Builder 6.0)**
JIRA CI-56
SVN: CardPro/TidManager/TID_v6
node: borland : ip=10.255.250.10 (win10 32x)
BINARIES: .dll, .exe, .bpl, .bmp
Builder: Borland C Builder 6.0
##
**Cборка mmsEOD (Borland C++ Builder 6.0)**
JIRA CI-58
SVN: MMS/mmsEOD
node: borland : ip=10.255.250.10 (win10 32x)
BINARIES: .dll, .exe, .bpl, .bmp
Builder: Borland C Builder 6.0
##
**Cборка PalmeraLoader (Borland C++ Builder 6.0)**
JIRA CI-59
SVN: Util/PalmeraLoader
node: borland : ip=10.255.250.10 (win10 32x)
BINARIES: .exe, .ini, .bat
Builder: Borland C Builder 6.0
##
**В интересах тестирования ТИЕТО**
##
**Перекладка скриптов из svn в nexus**
Полное название проекта: UCS/svn_nexus\n
Скрипты Пентахо для конвертирования в тестовые скрипты
JIRA: CI-57
SVN: NexusShareAsIs
node: jenkins-rosa : ip=172.16.10.236
##
**Преобразование скриптов .ktr/.kjb из svn в .xml для nexus**
Автотестирование sql запросов для релиза ТИЕТО MNR19
Полное название проекта: UCS/etl_nexus
JIRA: CI-60
SVN: TestSQLtoNexus
node: jenkins-rosa : ip=172.16.10.236

##
**Сборка микромодулей VT Linux**
Virtual terminal MM Jenkins declarative pipeline /Linux
JIRA: CI-62
SVN: VT/MicroModules

nodes: 
jenkins-rosa : ip=172.16.10.236 (rosa-linux, X64), label MICROMOD --only for test
jenkins-atol : 10.255.250.X (ubuntu 16.04 X86), label MICROMOD
jenkins-ubun : 10.255.250.X (ubuntu 18.04 X64), label MICROMOD
jenkins-fedora:  : 10.255.250.X (fedora34 5.13.19-200.fc34.X64), label MICROMOD
##
##
**Сборка микромодулей VT Win**
Virtual terminal MM Jenkins declarative pipeline /Windows
JIRA: CI-63
SVN: VT/MicroModules
nodes: borland : ip=10.255.250.10 (win10 32x)
##
Builder: MS Visual Studio 2013
##
shared-library in gitlab: /vars
NEXUS repo: http://10.255.250.50:8081/service/rest/repository/browse/orpo/
user=jenkucs_sa
SVN repo: https://svn/scm/svn/dev
user=jenkins