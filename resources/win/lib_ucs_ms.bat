cd %1
if %2==x64 ( copy .\Release\micros.lib ..\..\samples\test\bin64\ucs_ms.lib & sed -i s/bin\\ucs_ms.lib/bin64\\ucs_ms.lib/ ..\ucs_dt\ucs_dt.vcxproj)
if %2==Win32 ( copy .\Release\micros.lib ..\..\samples\test\bin\ucs_ms.lib )
