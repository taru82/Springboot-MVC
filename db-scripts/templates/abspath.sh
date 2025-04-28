#!/usr/bin/env bash
file_dir=$(dirname "$1")
cd $file_dir
echo $(pwd)/$(basename "$1")
