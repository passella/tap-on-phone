#!/bin/bash
set -e

DIR_ATUAL=$(pwd)

cd ../build
./build.sh

cd $DIR_ATUAL
./run.sh
