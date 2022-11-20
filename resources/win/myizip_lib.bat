pushd %1
if %2==x64 ( copy ..\..\..\myizip_z\x64\myizip_z.lib .\x64 )
if %2==Win32 ( copy ..\..\..\myizip_z\Release\myizip_z.lib .\Release )
