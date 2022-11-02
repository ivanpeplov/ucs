def call() {
    def yaml_cfg = readYaml file: "${WORKSPACE}/config/environment.yml"
        env.NEXUS_URL = yaml_cfg.get('nexus_url')
        env.NEXUS_URL_1 = yaml_cfg.get('nexus_url_1')
        env.SVN_URL = yaml_cfg.get('svn_url')
        env.MAIL_RECIPIENTS_DEV = yaml_cfg.get('mail_recipients_dev')
        env.BMP=yaml_cfg.get(APP).get('bmp')
        env.BPL=yaml_cfg.get(APP).get('bpl')
        env.EXE=yaml_cfg.get(APP).get('exe')

}
