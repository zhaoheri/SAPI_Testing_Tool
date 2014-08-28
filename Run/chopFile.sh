#!/bin/bash

#Created by Heri on 8/6/14.

#Usage: This shell script will split a large file into subfiles
#		./chopFile.sh [input file name] [output files series] [lines each file]
#		$1: readfile name
#		$2: output file name
#		$3: how many lines a sub file contains
#The output file name is in "0[outputFileName]", "1[outputFileName]"...

READFILE=$1
OUTPUTFILE=$2
count=0
fileNum=0
DONE=0

while [ $DONE -eq 0 ] 
do
	if [[ -f $fileNum$2 ]] ; then
		rm $fileNum$2
		let fileNum=$fileNum+1
  	else
  		DONE=1
    fi
done

fileNum=0
while read line || [ -n "$line" ]; do
     echo "$line" >> $fileNum$2
     let count=$count+1
     if [ $count -eq  $3 ]
     then
     	count=0
     	let fileNum=$fileNum+1
     fi
done < $READFILE
echo "$fileNum"