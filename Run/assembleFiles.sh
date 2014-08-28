#!/bin/bash

#Created by Heri on 8/6/14.

#Usage: This shell script assemble subfiles into one large file
#		./assembleFiles.sh [output file name] [input files series]
#		$1: output file name
#		$2: input files series

OUTPUTFILE=$1
INPUTFILE=$2
fileNum=0
DONE=0
rm $OUTPUTFILE
while [ $DONE -eq 0 ] 
do
	if [[ -f $fileNum$INPUTFILE ]] ; then
		# echo "found"
		while read -r line || [ -n "$line" ]; do
		     echo "$line" >> $OUTPUTFILE
		done < $fileNum$INPUTFILE
		let fileNum=$fileNum+1
  	else
  		# echo "not found"
  		DONE=1
    fi
done