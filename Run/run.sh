#!/bin/bash

#Created by Heri on 8/6/14.

#Usage: This shell script will split large replay.log file into many subfiles,
#		and call mod_log or mod_log_v2 to get sub .sh files.
#		Then run all the .sh files on yqlp or yql.
#		Finally, assemble all sub results to one file
#		./run.sh [yql/yqlp] [Resource Log File name] [Destination Result File name] [Logs number per file] (option)[HostName]

if [[ $# < 4 ]]; then
	echo "The params are too few. "
	echo "./run.sh [yql/yqlp] [Resource Log File name] [Destination Result File name] [Logs number per file] (option)[HostName]"
	exit
fi

model=$1
ResourceLogFile=$2
DestiResultFile=$3
LogsNumPerFile=$4
if [ "$#" = "5" ]; then
	HostName=$5
else
	if [ "$model" = "yql" ]; then
		HostName="video.query.yahoo.com"
	else
		HostName="video.media.yql.yahoo.com"
	fi
fi

fileNum=`./chopFile.sh $ResourceLogFile ResourceLogSub $LogsNumPerFile`
for (( i = 0; i <= $fileNum; i++ )); do
	if [[ -f $i"ResourceLogSub" ]] ; then
		if [ "$model" = "yql" ]; then
			./mod_log -o $i"SAPI.log" -n $HostName < $i"ResourceLogSub" > $i"SAPI.sh"
		else
			./mod_log_v2 -o $i"SAPI.log" -n $HostName < $i"ResourceLogSub" > $i"SAPI.sh"
		fi
		chmod +x $i"SAPI.sh"
		nohup ./$i"SAPI.sh" &
    fi
done

while [ true ]; do
	process=`jobs -l | grep Running`
	#echo $process
	if [ -z "$process" ] ; then
		break
	else
		sleep 1
	fi
done

 ./assembleFiles.sh $DestiResultFile".res" SAPI.log

 if [ "$model" = "yql" ]; then
	./mod_log -o "SAPI.log" -n $HostName < $ResourceLogFile > $DestiResultFile".sh"
else
	./mod_log_v2 -o "SAPI.log" -n $HostName < $ResourceLogFile > $DestiResultFile".sh"
fi

 rm *ResourceLogSub
 rm *SAPI.sh
 rm *SAPI.log