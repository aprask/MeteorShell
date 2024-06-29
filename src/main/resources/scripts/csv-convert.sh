#!/bin/bash

location=$(head -n 1 output.txt)
temp=$(tail -n 1 output.txt)

if [ ! -f "output.csv" ]; then
    echo "location, temp" > output.csv
fi

echo "$location, $temp" >> output.csv