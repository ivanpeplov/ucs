@Library("shared-library") _
properties([
  parameters([
      [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'Select LABEL',
      name: 'LABEL', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: 
            'return["armfm", "ppcfm", "fmman", "pseutils"]'
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'Select node to run',
      referencedParameters: 'LABEL',
      name: 'NODE_NAME', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
          if (LABEL=='armfm') {return ["eracom"]}
          if (LABEL=='ppcfm') {return ["jenkins-gem"]}
          if (LABEL == "pseutils" || LABEL == "fmman") {return ["borland"]}
          '''
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      referencedParameters: 'LABEL',
      name: 'CPROVDIR', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
            if (LABEL == "ppcfm") { return ["/opt/safenet/protecttoolkit5/ptk:selected:disabled"] }
            if (LABEL == "pseutils" || LABEL == "fmman") { return ["C:\\\\Progra~1\\\\SafeNet\\\\Protec~1\\\\Protec~1\\\\:selected:disabled"] }
            if (LABEL == "armfm") { return ["C:\\\\PROGRA~1\\\\Eracom\\\\PROTEC~1:selected:disabled"] }
            '''
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      referencedParameters: 'LABEL',
      name: 'FMDIR', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
            if (LABEL == "ppcfm") { return ["/opt/safenet/protecttoolkit5/fmsdk:selected:disabled"] }
            if (LABEL == "pseutils" || LABEL == "fmman") { return ["C:\\\\Progra~1\\\\SafeNet\\\\Protec~1\\\\Protec~1\\\\:selected:disabled"] }
            if (LABEL == "armfm") { return ["C:\\\\PROGRA~1\\\\Eracom\\\\PROTEC~2:selected:disabled"] }
            '''
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT',
      referencedParameters: 'LABEL', 
      name: 'TARGET', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script:
            'if (LABEL == "pseutils" || LABEL == "fmman") { return ["FMuX/output"] }'
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'Select TRUE for make debug=1 Build',
      referencedParameters: 'LABEL',
      name: 'DEBUG', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script:'''
           if (LABEL == "armfm" || LABEL == "ppcfm") { return ["", "TRUE"] }'''
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'obj-$(ARCH)d : $(OBJDIR) folder',
      referencedParameters: 'DEBUG',
      name: 'TAIL', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
            if (DEBUG == "TRUE") { return ["d:selected:disabled"] }
            '''
        ]
      ]
    ]
  ])
])
pipeline { //CI-52: armfm, ppcfm; CI-72: PSEutils.dll, fm_manager.dll
  agent {label NODE_NAME}
    //parameters {
    //booleanParam(name: "SIGN", defaultValue: false, description: 'Only for testing')
    //} //parameters end
  environment {
      //TARGET="FmUX/fm/obj-${ARCH}${TAIL}" //where find files for upload
      ROOT="PassKey/FM/FmUX"
      SVN_PATH = "${ROOT}" //full path for download fron SVN
      LD_LIBRARY_PATH="${CPROVDIR}/lib:"
      PTKBIN="${CPROVDIR}/bin"
      PTKLIB="${CPROVDIR}/lib"
      PATH_GEM="/usr/local/bin:/usr/bin:/bin:"
      INCLUDE="C:\\Program Files\\SafeNet\\Protect Toolkit 5\\FM SDK\\include;C:\\Program Files\\SafeNet\\Protect Toolkit 5\\Protect Toolkit C SDK\\include;C:\\Program Files\\SafeNet\\Protect Toolkit 5\\Protect Toolkit C SDK\\samples\\include;C:\\Program Files\\Windows Kits\\10\\Include\\10.0.19041.0\\um;C:\\Program Files\\Windows Kits\\10\\Include\\10.0.19041.0\\shared;C:\\Program Files\\Microsoft Visual Studio 12.0\\VC\\include"
      LIB="C:\\Program Files\\Windows Kits\\10\\Lib\\10.0.19041.0\\um\\x86;C:\\Program Files\\Microsoft Visual Studio 12.0\\VC\\lib"
      PATH = "C:\\Program Files\\Eracom\\ProtectToolkit C SDK\\bin;C:\\Program Files\\Eracom\\ProtectToolkit C SDK\\bin\\sw;C:\\Program Files\\Eracom\\ProtectProcessing Orange SDK\\bin;C:\\gcc-fm\\bin;C:\\Program Files\\SafeNet\\Protect Toolkit 5\\FM SDK\\bin;C:\\Program Files\\SafeNet\\Protect Toolkit 5\\Protect Toolkit C SDK\\bin;C:\\Program Files\\Microsoft Visual Studio 12.0\\VC\\bin;C:\\Program Files\\TortoiseSVN\\bin;c:\\jenkins\\bin;C:\\Windows\\system32;C:\\Program Files\\Eclipse Adoptium\\jre-11.0.16.101-hotspot\\bin;C:\\Program Files\\Java\\jre1.8.0_341\\bin;C:\\Program Files\\Git\\bin,C:\\Program Files\\Git\\cmd,${PATH_GEM}"
  } 
  stages {
      stage('SET Env') {
        steps {
          script {
            setDescription()
            setEnv()
          }
        }
      }
      stage ('PREPARE') {
        steps {
          script {
            getSVN()
            prepareFiles("${LABEL}")
          }
        }
      }
      stage('FM') {
        when { expression  { LABEL == "armfm" || LABEL == "ppcfm" } }
        steps {
          dir ('FmUX/fm') {
            script {
              switch (LABEL) {
              case "ppcfm": //gemalto
              TAIL=="" ? sh(script:"unset ${DEBUG} ; make") : sh(script:"make debug=1") //elvis operator   
              break
              default: //eracom
              TAIL=="" ? bat(script:"gnumake") : bat(script:"gnumake debug=1") //elvis operator          
              }
            }
          }
        }
      }
      stage('HOST') {
        when { expression  { LABEL == "pseutils" || LABEL == "fmman" } }
        steps {
          dir ('FMuX/host') {
            script {
              switch (LABEL) {
                case ("fmman") :
                  bat(script:"nmake -f nt-dll.mak")
                break
                case ("pseutils") :
                  bat(script:"nmake -f PSEutils-nt-dll.mak")
                break
              }
            }
          }
        }
      }
      /*stage('SIGN') {
        when {
          beforeInput true
          expression { return env.SIGN.toBoolean() }
        }
        input {
          message "Pin for ${ARCH} FM Sign-in"
          ok "Yes"
          parameters {
                  string(name: "SIGN", defaultValue: "222222")
          }
        }
        steps {
          dir (TARGET) {
            script {
              if (ARCH=='ppcfm') {sh "mkfm -k ABG/fm -ffmUX.bin -oFmUX.fm 
              else {bat "echo ${SIGN} | mkfm -k ABG/fm -ffmUX -ofmUX.fm"}
            }
          }
        }
      }*/
      stage('UPLOAD') {
        steps {
          script {
            if (LABEL=='armfm' || LABEL=='ppcfm')
            {TARGET="FmUX/fm/obj-${LABEL}${TAIL}"}
            uploadFiles("${LABEL}", "${TARGET}")
          }
        }
      }
  } //stages
  post {
    always { cleanWs() }
    failure { script { sendEmail() } }
  } //post
} //pipeline