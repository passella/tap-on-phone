#!/bin/bash
set -e

script_dir=$(dirname $(realpath "$0"))

"$script_dir"../buildbuild.sh

"$script_dir"/run.sh
