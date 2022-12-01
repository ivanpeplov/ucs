#!/bin/bash
#script to recursively travel a dir of n levels
#usage: ./dirN.sh dir
find . -type d -name "$1" -exec bash -c 'cd "$(dirname "{}")"; zip -r "$(basename "{}")".zip "$(basename "{}")"' \;