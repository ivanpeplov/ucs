#!/bin/bash
#script to recursively travel a dir of n levels
#usage: ./dirN.sh dir
function traverse() {   
    for file in $(ls "$1")
    do
        #current=${1}{$file}
        if [[ ! -d ${1}/${file} ]]; then
            echo " ${1}/${file} is a file"
        else
            #echo "entering recursion with: ${1}${file}"
            traverse "${1}/${file}"
        fi
    done
}
function main() {
    traverse "$1"
}
main "$1"