#!/bin/bash
# base method for SVN downloading
svn co --quiet --username jenkins --password ${svn_pwd} https://${SVN_URL}/${SVN_PATH}