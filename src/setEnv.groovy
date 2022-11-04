def call() {
    //${WORKSPACE}/git/ - each pipeline in WEB UI has option - checkout git to subfolder.
    def yaml_cfg = readYaml file: "${WORKSPACE}/git/config/environment.yml"
        env.NEXUS_URL = yaml_cfg.get('nexus_url')
        env.NEXUS_URL_1 = yaml_cfg.get('nexus_url_1')
        env.SVN_URL = yaml_cfg.get('svn_url')
        env.MAIL_RECIPIENTS_DEV = yaml_cfg.get('mail_recipients_dev')
        if (JOB_BASE_NAME=='mms_eod' || JOB_BASE_NAME=='tid_man' || JOB_BASE_NAME=='palmera') {
            env.bmp=yaml_cfg.get(APP).get('bmp')
            env.bpl=yaml_cfg.get(APP).get('bpl')
        }
}