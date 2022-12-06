@Library("shared-library") _
properties([
  parameters([
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'Select node to run',
      name: 'NODE_NAME', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
          label='FM'
          def nodes = jenkins.model.Jenkins.get().computers
          .findAll{ it.node.labelString.contains(label) }
          .collect{ it.node.selfLabel.name }
          return nodes
          '''
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'armfm - eracom, ppcfm - gemalto',
      referencedParameters: 'NODE_NAME',
      name: 'ARCH', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
            if (NODE_NAME == "jenkins-gem") { return ["ppcfm:selected:disabled"] }
            else { return ["armfm:selected:disabled"] }
            '''
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      referencedParameters: 'NODE_NAME',
      name: 'CPROVDIR', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
            if (NODE_NAME == "jenkins-gem") { return ["/opt/safenet/protecttoolkit5/ptk:selected:disabled"] }
            else { return ["C:\\\\PROGRA~1\\\\Eracom\\\\PROTEC~1:selected:disabled"] }
            '''
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      referencedParameters: 'NODE_NAME',
      name: 'FMDIR', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: '''
            if (NODE_NAME == "jenkins-gem") { return ["/opt/safenet/protecttoolkit5/fmsdk:selected:disabled"] }
            else { return ["C:\\\\PROGRA~1\\\\Eracom\\\\PROTEC~2:selected:disabled"] }
            '''
        ]
      ]
    ],
    [$class: 'CascadeChoiceParameter', 
      choiceType: 'PT_SINGLE_SELECT', 
      description: 'Select TRUE for make debug=1 Build',
      name: 'DEBUG', 
      script: [
        $class: 'GroovyScript', 
        script: [
          classpath: [], 
          sandbox: false, 
          script: 
            'return ["", "TRUE"]'
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
pipeline { //CI-52
  agent {label NODE_NAME}
  options { timeout(time: 10, unit: 'MINUTES') }
    //parameters {
    //booleanParam(name: "SIGN", defaultValue: false, description: 'Only for testing')
    //} //parameters end
  environment {
      TARGET="FmUX/fm/obj-${ARCH}${TAIL}" //where find files for upload
      ROOT='PassKey/FM/FmUX' //project root at SVN
      SVN_PATH = "${ROOT}" //full path for download fron SVN
      LD_LIBRARY_PATH="${CPROVDIR}/lib:"
      PTKBIN="${CPROVDIR}/bin"
      PTKLIB="${CPROVDIR}/lib"
      PATH_GEM="/usr/local/bin:/usr/bin:/bin:"
      PATH = "C:\\Program Files\\TortoiseSVN\\bin;c:\\jenkins\\bin;C:\\Windows\\system32;C:\\Program Files\\Eclipse Adoptium\\jre-11.0.16.101-hotspot\\bin;C:\\Program Files\\Eracom\\ProtectToolkit C SDK\\bin;C:\\Program Files\\Eracom\\ProtectToolkit C SDK\\bin\\sw;C:\\Program Files\\Eracom\\ProtectProcessing Orange SDK\\bin;c:\\gcc-fm\\bin;C:\\Program Files\\Java\\jre1.8.0_341\\bin;C:\\Program Files\\Git\\bin,C:\\Program Files\\Git\\cmd,${PATH_GEM}"
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
            prepareFiles('fm')
          }
        }
      }
      stage('BUILD') {
        steps {
          dir ('FmUX/fm') {
            script {
              switch (ARCH) {
              case "ppcfm": //gemalto
              //obligatory for single makefile support
              //sh "sed -i 's;\\\\samples\\\\;/samples/;' Makefile"
              loadScript(place:'fmux', name:'Makefile')
              loadScript(place:'fmux', name:'cfgbuild.mak')
              sh "cp ./cfgbuild.mak ${WORKSPACE}/FmUX/"
              TAIL=="" ? sh(script:"unset ${DEBUG} ; make") : sh(script:"make debug=1") //elvis operator   
              break
              default: //eracom
              TAIL=="" ? bat(script:"gnumake") : bat(script:"gnumake debug=1") //elvis operator          
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
          script {
            hsmSign("${TARGET}", "${ARCH}")
          }
        }
      }*/
      stage('UPLOAD') {
        steps {
          script {
            uploadFiles("${ARCH}", "${TARGET}")
          }
        }
      }
  } //stages
  post {
    always {
      script {             
        echo 'Clean Workspace'
        cleanWs()
      }//script
    }//always
    failure {
        script {
          //emailing
          sendEmail()               
        }//script
    }//failure
  } //post actions
} //pipeline