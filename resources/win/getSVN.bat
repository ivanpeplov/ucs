@echo off
rem base method for snv downloading
svn co --quiet --username jenkins --password=%svn_pwd% --non-interactive --trust-server-cert-failures=unknown-ca,cn-mismatch,expired,not-yet-valid,other https://%SVN_URL%/%SVN_PATH%
        