SAPI_Testing_Tool
=================

####Introduction  
* This testing tool is built to test new SAPI, and make sure the behaviour of new SAPI is the same with old SAPI. 
* The old SAPI (Javascript) is based on YQL, and the new SAPI (Java) is base on YQLP.
* The testing tool is almost like a simulator, which simulates the users’ requests using curl command.
* Whole flowchart  
<img src="https://raw.githubusercontent.com/zhaoheri/SAPI_Testing_Tool/raw/master/ReadmePicture/flowchart.png" alt="Drawing" width=500px/>
  1. Firstly, convert access log to YQL format and YQLP format respectively. The results are same content in different formats: Curl command in YQL format and in YQLP format.
  2. Run two formats curl commands respectively, then get two responses in Json format.
  3. Java program is used to compare every field between two responses, and then outputs the differences, their number, and one of difference example for debug.
* One log example:
  1. Access Log combination: [path&parameters] [YahooRemoteIP] [UserAgent]    
  >
  >/v1/public/yql?q=SELECT%20*%20FROM%20yahoo.media.video.streams%20WHERE%20id%3D%22976f23fe-3c45-3dd7-88de-f0bffdd12864%22%20AND%20format%3D%22m3u8%2Cmp4%2Cflv%2Cf4m%22%20AND%20protocol%3D%22http%2Crtmp%22%20AND%20rt%3D%22flash%22%20AND%20plrs%3D%22qu1Bx_NvNTeAF8p63S5.q0%22%20AND%20acctid%3D%22350%22%20AND%20plidl%3D%22default%22%20AND%20pspid%3D%22794003033%22%20AND%20offnetwork%3D%22false%22%20AND%20site%3D%22news%22%20AND%20lang%3D%22zh-Hant-TW%22%20AND%20region%3D%22TW%22%20AND%20override%3D%22none%22%20AND%20plist%3D%22%22%20AND%20hlspre%3D%22true%22%20AND%20ssl%3D%22false%22%20AND%20synd%3D%22%22%3B&env=prod&format=json&callback=YUI.Env.JSONP.yui_3_9_1_1_1407787205542_679    59.127.91.124    Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko

  2. Curl command combination: curl -G “[path&parameters]” -H “[YahooRemoteIP]” -H “[User-Agent]” -H “[Host]”  
  >YQL:  
  >curl -G "http://video.query.yahoo.com/v1/public/yql?q=SELECT%20*%20FROM%20yahoo.media.video.streams%20WHERE%20id%3D%22976f23fe-3c45-3dd7-88de-f0bffdd12864%22%20AND%20format%3D%22m3u8%2Cmp4%2Cflv%2Cf4m%22%20AND%20protocol%3D%22http%2Crtmp%22%20AND%20rt%3D%22flash%22%20AND%20plrs%3D%22qu1Bx_NvNTeAF8p63S5.q0%22%20AND%20acctid%3D%22350%22%20AND%20plidl%3D%22default%22%20AND%20pspid%3D%22794003033%22%20AND%20offnetwork%3D%22false%22%20AND%20site%3D%22news%22%20AND%20lang%3D%22zh-Hant-TW%22%20AND%20region%3D%22TW%22%20AND%20override%3D%22none%22%20AND%20plist%3D%22%22%20AND%20hlspre%3D%22true%22%20AND%20ssl%3D%22false%22%20AND%20synd%3D%22%22%3B&env=prod&format=json" -H "YahooRemoteIP: 59.127.91.124" -H "User-Agent:     Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko" -H "Host:video.query.yahoo.com"  
  >
  >YQL:  
  >curl -G "http://video.query.yahoo.com/v1/public/yql?q=SELECT%20*%20FROM%20yahoo.media.video.streams%20WHERE%20id%3D%22976f23fe-3c45-3dd7-88de-f0bffdd12864%22%20AND%20format%3D%22m3u8%2Cmp4%2Cflv%2Cf4m%22%20AND%20protocol%3D%22http%2Crtmp%22%20AND%20rt%3D%22flash%22%20AND%20plrs%3D%22qu1Bx_NvNTeAF8p63S5.q0%22%20AND%20acctid%3D%22350%22%20AND%20plidl%3D%22default%22%20AND%20pspid%3D%22794003033%22%20AND%20offnetwork%3D%22false%22%20AND%20site%3D%22news%22%20AND%20lang%3D%22zh-Hant-TW%22%20AND%20region%3D%22TW%22%20AND%20override%3D%22none%22%20AND%20plist%3D%22%22%20AND%20hlspre%3D%22true%22%20AND%20ssl%3D%22false%22%20AND%20synd%3D%22%22%3B&env=prod&format=json" -H "YahooRemoteIP: 59.127.91.124" -H "User-Agent:     Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko" -H "Host:video.query.yahoo.com"
  
  3. Check the two responses in your broswser: (Just the http part in the curl command)  
    * [YQL response](http://video.query.yahoo.com/v1/public/yql?q=SELECT%20*%20FROM%20yahoo.media.video.streams%20WHERE%20id%3D%22976f23fe-3c45-3dd7-88de-f0bffdd12864%22%20AND%20format%3D%22m3u8%2Cmp4%2Cflv%2Cf4m%22%20AND%20protocol%3D%22http%2Crtmp%22%20AND%20rt%3D%22flash%22%20AND%20plrs%3D%22qu1Bx_NvNTeAF8p63S5.q0%22%20AND%20acctid%3D%22350%22%20AND%20plidl%3D%22default%22%20AND%20pspid%3D%22794003033%22%20AND%20offnetwork%3D%22false%22%20AND%20site%3D%22news%22%20AND%20lang%3D%22zh-Hant-TW%22%20AND%20region%3D%22TW%22%20AND%20override%3D%22none%22%20AND%20plist%3D%22%22%20AND%20hlspre%3D%22true%22%20AND%20ssl%3D%22false%22%20AND%20synd%3D%22%22%3B&env=prod&format=json)  
    * [YQLP response](http://video.media.yql.yahoo.com:4080/v1/video/sapi/streams/976f23fe-3c45-3dd7-88de-f0bffdd12864?format=m3u8,mp4,flv,f4m&protocol=http,rtmp&rt=flash&plrs=qu1Bx_NvNTeAF8p63S5.q0&acctid=350&plidl=default&pspid=794003033&offnetwork=false&site=news&lang=zh-Hant-TW&region=TW&override=none&plist=&hlspre=true&synd=)
  
  4. Compare the two responses to see if they are the same or not, using the Java program.


####How to use
* Files List:
  1. mod_log.cpp: YQL format converter
  2. mod_log_v2.cpp: YQLP format converter
  3. json_comp Java project: Java program used to compare Json responses
  4. assembleFile.sh: Assemble any series files into one file
  5. chopFile.sh: Chop any file into series files by lines
  6. run.sh: Run curl commands and output the responses for YQL or YQLP
* File usage:
  1. mod_log.cpp & mod_log_v2.cpp  
	* After any change, compile this two files:  
	  $make
    * Convert to YQL format:  
      $./mod_log --outputFileName 0826_yql.res --hostname video.media.yql.yahoo.com < [LogFileName]  
      @param: --outputFileName --hostname  
	  option param: --sleep
	* Convert to YQLP format:  
	  $./mod_log --outputFileName 0826_yql.res --hostname video.media.yql.yahoo.com < [LogFileName]
  2. run,sh:
    * Automatically change format and run curl commands and output the responses and curl command files. It helps you do all the things below:  
    	<img src="https://raw.githubusercontent.com/zhaoheri/SAPI_Testing_Tool/raw/master/ReadmePicture/whatRunDoes.png" alt="Drawing" width=300px> 
    * Architecture:  
      Access Log -> log1/log2….. -> change format -> 1curl.sh/2curl.sh…. -> run in the same time -> 1.res/2.res…. -> all.res
    * @param:  
	  [yql/yqlp] run model, yql or yqlp. For example, if it is yql, it will generate yql format commands and run them on SAPI(yql).  
	  [Resource Log File Name] original access log file.  
	  [Destination Result File name] Output file, all responses from yql or yqlp will be in this file.  
	  [Logs number per file] This shell script will chop one log file with a lot of logs into several sub log files, so that we can run them in the same time, and save a lot of time.  
	  (option) [HostName] This param is for the url hostname, you can change it to any host name you want because the testing might process on different servers. The default hostname is video.query.yahoo.com(yql)/video.media.yql.yahoo.com(yqlp).    
		
    	>eg. YQL url:    
		>curl -G "http://**_[HostName]_**/v1/public/yql?q=SELECT%20*%20FROM%20yahoo.media.video.streams%20%20WHERE%20id%3D%227a99a31f-bcaa-3d7f-9bf5-39d27c4719bf%22%20%20AND%20plrs%3D%22%22%20%20AND%20acctid%3D%22863%22%20%20AND%20width%3D%22640%22%20AND%20height%3D%22360%22%20%20AND%20site%3D%22ivy%22%20%20AND%20lang%3D%22en-US%22%20%20AND%20region%3D%22US%22%20AND%20resize%3D%22true%22&env=prod&format=json&bucket=711020&cacheable=1&ssl=true" -H "YahooRemoteIP: 144.46.104.12" -H "User-Agent:     Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)" -H "Host:video.query.yahoo.com"
		>
		>eg. YQLP url:    
		>curl -G "http://**_[HostName]_**:4080/v1/video/sapi/streams/7a99a31f-bcaa-3d7f-9bf5-39d27c4719bf?plrs=&acctid=863&width=640&height=360&site=ivy&lang=en-US&region=US&resize=true" -H "YahooRemoteIP: 144.46.104.12" -H "User-Agent:     Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)" -H "Host:video.media.yql.yahoo.com"    
	* To Run:  
	  $./run.sh [yql/yqlp] [Resource Log File name] [Destination Result File name] [Logs number per file] (option)[HostName]  
	  Should see :  
	  <img src="https://raw.githubusercontent.com/zhaoheri/SAPI_Testing_Tool/raw/master/ReadmePicture/whatsOnTerminalWhenRun.png" alt="Drawing" width=600px/>
	  The number of “appending output the nohup.out” corresponding to the number of sub files.    
	  The curl information will be recorded in the file nohup.out:    
	  <img src="https://raw.githubusercontent.com/zhaoheri/SAPI_Testing_Tool/raw/master/ReadmePicture/nohup.png" alt="Drawing" width=600px/>
	* Output file:
   
		>$./run.sh  yqlp  0827.log  0827_yqlp  3  video.media.yql.yahoo.com    

	  There will be two output files:   
	  	0827_yqlp.sh: all curl command in yqlp format    
	  	0827_yqlp.res: all yqlp responses    
  3. json_comp Java project:
  	* In the main function, change the yqlpFileName and yqlFileName variables to the file names that you want to compare.
		
		>eg. String yqlFileName = "0812_yql.res";    
		>eg. String yqlpFileName = "0812_yqlp.res";    
	* Also, change the accessLogFolder to the path where yql and yqlp files exist.
		
		>eg. String accessLogFolder = "/Users/herizhao/workspace/accessLog/";
	* Then you can run to compare. The current line number will show up in the console.
	* Architecture:
		1. Modify some fields in YQLP:   
		   yvap -> empty, because yvap is for ads and it might different  
		   streams: h264_profile -> empty  
		   streams: is_primary default value -> false  
		   meta: show_name, event_start, event_stop default value -> empty  
		   meta: credit: label -> empty  
		   metrics: plidl, isrc, pspid -> empty  
		2. Modify some fields in YQL:  
		    yvap -> empty, because yvap is for ads and it might different  
		3. Compare YQL and YQLP:  
			<img src="https://raw.githubusercontent.com/zhaoheri/SAPI_Testing_Tool/raw/master/ReadmePicture/json_compFlowChart.png" alt="Drawing" width=900px/>  
		4. Print out the difference result:  
		   First print out the titles, and then print the difference list.


####How to see the compare result:
Import the .txt file you got from java program into Excel.  
<img src="https://raw.githubusercontent.com/zhaoheri/SAPI_Testing_Tool/raw/master/ReadmePicture/compareResult1.png" alt="Drawing" width=1000px/>  
This is comparision for every field in streams. You can see the yql result and yqlp result in cells if they are different.  
The lineNumForStream refers to the line number in curl command .sh file, where you can find the curl command corresponding to this ID and this problem. (eg. in the chart above, if you go for line 23608 in the 8027_yql.sh and 0827_yqlp.sh, and run both curl again, you will find the host and path are different.)  
<img src="https://raw.githubusercontent.com/zhaoheri/SAPI_Testing_Tool/raw/master/ReadmePicture/compareResult2.png" alt="Drawing" width=1000px/>  
This is comparision for every field in meta. The representation method is the same with streams. In this example, lineNum:0 means there is no difference in the meta, so this ID must be picked up by other reasons.  
<img src="https://raw.githubusercontent.com/zhaoheri/SAPI_Testing_Tool/raw/master/ReadmePicture/compareResult3.png" alt="Drawing" width=1000px/>  
This is the last part of the table. streamsDiffNum, metaDiffNum, metricsDiffNum, licenseDiffNum, and closedcaptionDiffNum represent how many differences caused by this ID. (eg. In the chart above, this ID caused 604 streams differences, and 0 for the others.)  


####How to reserve testing server:
Reserve YQLP server:  
https://docs.google.com/a/yahoo-inc.com/spreadsheet/ccc?key=0AmhobAr20GhOdENtenY1MHJWanpUaWlNUjFmM3Y4bHc&usp=drive_web#gid=4  
Just fill in when you want to use the sever. Up to 4 hours for one period.  
Reserve YQL server:  
Priyanka Tarar could help to set up the YQL testing server.  
http://bug.corp.yahoo.com/show_bug.cgi?id=7069698  
