def call() {
    //${WORKSPACE}/git/ - each pipeline in WEB UI has option - checkout git to subfolder.
    def yaml_cfg = readYaml file: "${WORKSPACE}/git/config/environment.yml"
        env.NEXUS_URL = yaml_cfg.get('nexus_url')
        env.NEXUS_URL_1 = yaml_cfg.get('nexus_url_1')
        env.NEXUS_URL_TEST = yaml_cfg.get('nexus_url_test')
        env.SVN_URL = yaml_cfg.get('svn_url')
        env.MAIL_RECIPIENTS_DEV = yaml_cfg.get('mail_recipients_dev')
        switch (JOB_BASE_NAME) {
            case ['mms_eod', 'tid_man', 'palmera'] :
                env.bmp=yaml_cfg.get(APP).get('bmp')
                env.bpl=yaml_cfg.get(APP).get('bpl')
            break
            case ['mm_nix', 'mm_win'] :
                env.mm=yaml_cfg.get(APP).get('mm')
                env.mmm=yaml_cfg.get(APP).get('mmm')
                env.id=yaml_cfg.get(APP).get('id')
            break
            default :
            println "For Non-Borland C++ Builder pipelines"
        }
}
