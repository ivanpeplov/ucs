#!/bin/bash
#chk_sql.groovy
# change " " to "_" in filenames recursively
find -name "* *" -print0 | sort -rz | \
  while read -d $'\0' f; do mv -v "$f" "$(dirname "$f")/$(basename "${f// /_}")";
  done