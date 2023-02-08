@echo off
rem mm_win.groovy
cd setup_p & devenv setup_p.sln /build Release
copy .\Release\setup_p.msi ..\rel_p

