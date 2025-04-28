#!/bin/bash
set -a

SCRIPT_DIR=$( dirname "${BASH_SOURCE[0]}" )


GREEN_="\033[32m"
_GREEN="\033[0m"
RED_="\033[31m"
_RED="\033[0m"
BOLD_="\033[1m"
_BOLD="\033[0m"
CYAN_="\033[36m"
_CYAN="\033[0m"


__DONE(){
    echo -e "${GREEN_}[INFO]: DONE${_GREEN}"
}

__SUCCESS(){
    echo -e "${GREEN_}[SUCCESS]${_GREEN}: $@"
}

__INFO(){
    echo -e "${CYAN_}[INFO]${_CYAN}: $@"
}

__WARN(){
    echo -e "${BOLD_}[WARNING]${_BOLD}: $@"
}

__ERROR(){
    echo -e "${RED_}[ERROR]${_RED}: $@"
}

__DEBUG(){
    if [ "$DEBUG" == "y" ]
    then
        INFO "[DEBUG]: $@"
    fi
}



#for mac OSX : brew install gnu-sed
function to_class_name (){
    local str="$1"
    #replace _ with dash
    str=$(echo $str | sed -E "s/\_/-/g")
    #multiple dash into single dash
    str=$(echo $str | sed -E "s/\-+/-/g")
    local result=""
    IFS='-' read -ra PART <<< "$str"
    for part in "${PART[@]}"; do
        lower=$(echo "$part" | tr '[:upper:]' '[:lower:]')
        Camel="$(tr '[:lower:]' '[:upper:]' <<< ${lower:0:1})${lower:1}"
        result=$result"$Camel"
    done
    echo $result
}



