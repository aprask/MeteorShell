#!/bin/bash
#clean the txt file 'unclean-output.txt' into a csv file with only temp and location

if [ -z "$1" ]; then
  echo "Usage: $0 input.txt"
  exit 1
fi

input_file="$1"

temp=$(grep -oP 'Right now it is \K[0-9]+' "$input_file")

location=$(grep -oP '(?<=Weather data has been scraped for )\w+' "$input_file")

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

if [ "$?" = 0 ]
then
  echo "Location: $location"
  echo "Temperature: $temp"
fi