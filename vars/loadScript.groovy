def call(Map script=[:]) { //for all jenkinsfile
  def scriptcontents = libraryResource "${script.place}/${script.name}"    
  writeFile file: "${script.name}", text: scriptcontents 
  if (script.place=='linux') {sh "chmod a+x ./${script.name}"}
}