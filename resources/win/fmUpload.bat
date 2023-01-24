@echo off
rem fm
if %LABEL%==armfm goto arm_fm
if %LABEL%==pseutils goto pseutils
if %LABEL%==fmman goto fmman
:arm_fm 
7z a %LABEL%.zip fmUX*, FmUX* -x!fmUpload.bat
curl  -u jenkucs_sa:%nexus_pwd% --upload-file %LABEL%.zip  %NEXUS_URL_1%/FM/
goto end
:pseutils
7z a %LABEL%.zip PSEutils.dll -x!fmUpload.bat
curl  -u jenkucs_sa:%nexus_pwd% --upload-file %LABEL%.zip  %NEXUS_URL_1%/FM/
goto end
:fmman
7z a %LABEL%.zip fm_manager.dll -x!fmUpload.bat
curl  -u jenkucs_sa:%nexus_pwd% --upload-file %LABEL%.zip  %NEXUS_URL_1%/FM/
:end

                
