@echo off
7z a setup_p.zip *
curl  -s -u jenkucs_sa:%nexus_pwd% --upload-file setup_p.zip  %NEXUS_URL%/%TOOR%/
                
