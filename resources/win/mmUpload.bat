@echo off
curl  -s -u jenkucs_sa:%nexus_pwd% --upload-file setup_p.zip  %NEXUS_URL%/%TOOR%/
                
