@echo off
if not %1==ucs_mm goto not_ucs_mm
if %1==ucs_mm goto ucs_mm 
:not_ucs_mm
cd %1 & msbuild %1.sln /t:build /p:configuration=Release /p:Platform=%2
goto end
:ucs_mm
cd %1 & msbuild %1.sln /t:build /p:configuration=Release /p:Platform=%2
:end
