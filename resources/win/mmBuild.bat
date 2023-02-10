@echo off
rem mm_win.groovy
rem micros.lib to ucs.ms_lib - prelim step for ucs_dt.exe building && fix at ucs_dt.vcxproj for x64
cd %1
msbuild %1.sln /t:build /p:configuration=Release /p:Platform=%2
if not %1==ucs_ms goto EOF
rem if %1==ucs_ms goto lib
rem :lib
if %2==x64 ( copy .\Release\micros.lib ..\..\samples\test\bin64\ucs_ms.lib & sed -i s/bin\\ucs_ms.lib/bin64\\ucs_ms.lib/ ..\ucs_dt\ucs_dt.vcxproj)
if %2==Win32 ( copy .\Release\micros.lib ..\..\samples\test\bin\ucs_ms.lib )
:EOF