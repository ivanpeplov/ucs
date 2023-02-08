@echo off
rem mm_win.groovy
7z a setup_p.zip * -x!mmUpload.bat
curl -u jenkucs_sa:%nexus_pwd% --upload-file setup_p.zip  %NEXUS_URL%/%TOOR%/
                
