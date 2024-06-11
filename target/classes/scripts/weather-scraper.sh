#!/bin/bash
#weather scraper for weather data into txt

#CHECKING FOR CURL
if ! command -v curl &> /dev/null; then
    echo "curl not found. Please install curl."
    exit 1
fi

#IF PREVIOUS RUN
rm -f unclean-output.txt

#CHECKING PARAMETERS (must be equal to 3 parameters)
if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <location>"
    exit 1
fi

#PARAMETERS
location="$1"
curl=$(which curl)
url="https://kidsweatherreport.com/report/$location"

#LOGIC
outfile="output.txt"

function dump_webpage() {
  $curl -o $outfile "$url"
  check_for_err
}

function check_for_err() {
  [ $? -ne 0 ] && echo "ERROR: Cannot download requested page" && exit 1
}

function strip_html() {
  grep "Right now it is" "$outfile" | sed 's/<[^>]*>//g' | sed 's/^[ \t]*//' > tmp.txt && cp tmp.txt "$outfile"
}

function add_location() {
  echo "$location" >> "$outfile"
}

function get_weather() {
  while read weather; do
    echo "${weather}"
  done < "$outfile"
}

###############################
#           MAIN              #
###############################
dump_webpage
strip_html
add_location
get_weather

rm tmp.txt

echo "Weather data has been scraped for $location"

#END