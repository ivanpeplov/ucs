pushd %1
msbuild %1.sln /t:build /p:configuration=REL_UNIEXE_REPOS /p:Platform=%2


