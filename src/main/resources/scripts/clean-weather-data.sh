#!/bin/bash
#clean the txt file 'unclean-output.txt'

if [ -z "$1" ]; then
  echo "Usage: $0 input.txt"
  exit 1
fi

input_file="$1"

temp=$(grep -oP 'Right now it is \K[0-9]+' "$input_file")

location=$(awk '/Right now it is/ { getline; print }' "$input_file")

rm -f output.txt
outfile="output.txt"

function add_location() {
  echo "$location" >> "$outfile"
}

function add_temp() {
  echo "$temp" >> "$outfile"
}

###############################
#           MAIN              #
###############################
add_location
add_temp

rm -f unclean-output.txt

echo "$location"
echo "$temp"