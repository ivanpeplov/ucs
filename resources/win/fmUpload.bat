@echo off
rem fm
7z a %ARCH%.zip fmUX*, FmUX* -x!fmUpload.bat
curl  -u jenkucs_sa:%nexus_pwd% --upload-file %ARCH%.zip  %NEXUS_URL_1%/FM/
                
