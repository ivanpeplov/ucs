@echo off
if not %1==ucs_mm goto other
if %1==ucs_mm goto mm 
:other
cd %1 & msbuild %1.sln /t:build /p:configuration=Release%3 /p:Platform=%2
goto end
:mm
cd %1 & msbuild %1.sln /t:build /p:configuration=REL_UNIEXE_REPOS /p:Platform=%2
:end
