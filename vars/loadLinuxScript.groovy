def call(String name) { 
  def scriptcontents = libraryResource "linux/${name}"    
  writeFile file: "${name}", text: scriptcontents 
  sh "chmod a+x ./${name}"
}