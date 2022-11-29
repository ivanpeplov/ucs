@echo off
cd setup_p & devenv setup_p.sln /build Release
copy .\Release\setup_p.msi ..\rel_p
cd ..\rel_p & 7z a setup_p.zip *
