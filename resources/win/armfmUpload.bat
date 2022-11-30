@echo off
7z a %ARCH%_%BUILD_NUMBER%.zip fmUX*, FmUX* -x!armfmUpload.bat
curl  -u jenkucs_sa:%nexus_pwd% --upload-file %ARCH%_%BUILD_NUMBER%.zip  %NEXUS_URL_1%/FM/
                
