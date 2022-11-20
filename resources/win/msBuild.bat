pushd %1
msbuild %1.sln /t:build /p:configuration=Release /p:Platform=%2



