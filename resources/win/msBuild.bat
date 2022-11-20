pushd %1
msbuild %1.sln /t:build /p:configuration=Release%3 /p:Platform=%2



