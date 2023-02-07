#### FIS server
##### FIS host
```
FIS server C++ Jenkins declarative pipeline
JIRA: CI-51
SVN: FIS/new/
NEXUS: FIS/
Nodes:
jenkins-legacy : 172.16.10.235
jenkins-rosa : 172.16.10.236
```
##### Утилиты, относящиеся к проекту FIS
```
JIRA: CI-51
SVN: FIS/new/
NEXUS: FIS/
Nodes:
jenkins-legacy : 172.16.10.235
jenkins-rosa : 172.16.10.236
```
#### Сборка функциональных модулей для HSM
##### сборка функц. модулей для HSM Eracom
```
JIRA: CI-52
SVN: PassKey/FM/FmUX
NEXUS: FM/
Node: eracom : 172.24.31.199 (Win7, 32x)
```
##### сборка функц. модулей для HSM Gemalto
```
JIRA: CI-52
SVN: PassKey/FM/FmUX
NEXUS: FM/
Node: jenkins-gem : 172.16.64.70 (SUSE 12.5)
```
#### Сборка функциональных модулей (Borland C++ Builder 6.0)
##### Cборка TID Manager (Borland C++ Builder 6.0)
```
JIRA CI-56
SVN: CardPro/TidManager/TID_v6
NEXUS: BORLAND/
Node: borland : 10.255.250.10 (win10 32x)
Builder: Borland C Builder 6.0
```
##### Cборка mmsEOD (Borland C++ Builder 6.0)
```
JIRA CI-58
SVN: MMS/mmsEOD
NEXUS: BORLAND/
Node: borland : 10.255.250.10 (win10 32x)
Builder: Borland C Builder 6.0
```
##### Cборка PalmeraLoader (Borland C++ Builder 6.0)
```
JIRA CI-59
SVN: Util/PalmeraLoader
NEXUS: BORLAND/
Node: borland : 10.255.250.10 (win10 32x)
Builder: Borland C Builder 6.0
```
##### Cборка Passkey.exe (Borland C++ Builder 6.0)
```
JIRA CI-72
SVN: Passkey/Passkey
NEXUS: BORLAND/
Node: borland : 10.255.250.10 (win10 32x)
```
##### Cборка PSEutils.dll; fm_manager.dll (VC2013)
```
JIRA CI-72
SVN: Passkey/Passkey
NEXUS: FM/
Node: borland : 10.255.250.10 (win10 32x)
```
#### Тестирование релиза ТИЕТО MNR19
##### Перекладка скриптов из svn в nexus
```
Полное название проекта: UCS/scripts2nexus
Скрипты Пентахо для конвертирования в тестовые скрипты
JIRA: CI-57
SVN: NexusShareAsIs
NEXUS: NexusShareAsIs/
Node: jenkins-rosa : 172.16.10.236
```
##### Преобразование скриптов .ktr/.kjb из svn в .xml для nexus
```
Автотестирование sql запросов для релиза ТИЕТО MNR19
Полное название проекта: UCS/etl_nexus
JIRA: CI-60
SVN: TestSQLtoNexus
NEXUS: TestSQLtoNexus/
node: jenkins-rosa : 172.16.10.236
```
#### MicroModules
##### Linux
```
JIRA: CI-62
SVN: VT/MicroModules
NEXUS: MicroModule/Linux/
Nodes: label MICROMOD
jenkins-ubuntu-64 : 172.16.10.234 (ubuntu 18.04, 64)
jenkins-ubuntu-32 : 172.16.10.233 (ubuntu 16.04, 32)
jenkins-fedora : 10.255.250.62 (fedora28, 64)
jenkins-rosa : 172.16.10.236 (rosa-linux, 64), test
```
##### Win
```
JIRA: CI-63
SVN: VT/MicroModules
NEXUS: MicroModule/Windows/
Nodes: borland : 10.255.250.10 (win10 32x)
Builder: MS Visual Studio 2013
```
##### Java/Android
```
JIRA: CI-69/CI-70; CI-73/CI-743
SVN:
VT/MicroModuleJava/mmcore/trunk/mmcore (Maven)
VT/MicroModuleJava/android/trunk/mmlibrary (Gradle)
VT/MicroModuleJava/android/trunk/app (Gradle)
VT/MicroModuleJava/evotor (Gradle)
NEXUS: MicroModule/ANDROID/
Nodes: jenkins-fedora : 10.255.250.62 (fedora28  X64), label JAVA - production
JDK build 11.0.14+9-LTS, GRADLE 6.1.1, Maven 3.8.6, Latest Android SDK cmdline-tools for Linux
```
##### General URL
```
NEXUS orpo: http://10.255.250.50:8081/service/rest/repository/browse/orpo/
NEXUS Maven: http://172.16.10.230:3389/nexus/service/local/repositories/ucs_repo/content
user=jenkucs_sa
SVN repo: https://172.16.10.230/scm/svn/dev
user=jenkins
```