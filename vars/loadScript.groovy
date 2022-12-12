def call(Map script=[:]) { 
  def scriptcontents = libraryResource "${script.place}/${script.name}"    
  writeFile file: "${script.name}", text: scriptcontents 
  if (script.place=='linux') {sh "chmod a+x ./${script.name}"}
  if (script.place=='gradle') {sh "mv ${script.name} build.gradle"}
}