def call(String name) { 
  def scriptcontents = libraryResource "win/${name}"    
  writeFile file: "${name}", text: scriptcontents
}