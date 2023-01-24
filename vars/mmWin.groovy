def call (String yml, String arch) {
    smpl=yml.split(',')
    id=arch.split(',')
    id.each { x-> smpl.each { y-> 
        bat (script: "cd ${y} & msbuild ${y}.sln /t:build /p:configuration=Release /p:Platform=${x}") 
        if (y=='ucs_ms') {
            loadScript(place:'win', name:'mmLib.bat') 
            bat(script:"mmLib.bat ${y} ${x}" ) } } }
}