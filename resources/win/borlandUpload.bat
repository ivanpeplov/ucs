@echo off
if not "%~3"=="" goto svn
:svn
7z a %1_%2.zip *
curl -u jenkucs_sa:%nexus_pwd% --upload-file %1_%2.zip  %NEXUS_URL%/Borland/
goto end
7z a %1_%2_%3.zip *
curl -u jenkucs_sa:%nexus_pwd% --upload-file %1_%2_%3.zip  %NEXUS_URL%/Borland/
:end
                
