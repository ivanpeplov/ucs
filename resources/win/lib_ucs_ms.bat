@echo off
cd %1
if %2==x64 ( copy .\x64\REL_DLL\ucs_ms.lib ..\..\samples\test\bin\ )
if %2==Win32 ( copy .\REL_DLL\ucs_ms.lib ..\..\samples\test\bin\ )
