package json_cmp;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.ChangedCharSetException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class Comparer {
	
	public static void FloatingtoInt(ObjectNode obj, String str) {
		  //System.out.println("	" + str + " " + obj.path(str).asDouble());
		  BigDecimal temp = new BigDecimal(obj.path(str).asDouble()).setScale(0,  BigDecimal.ROUND_HALF_UP);
		  //int temp = (int) Math.ceil(obj.path(str).asDouble());
		  ((ObjectNode) obj).put(str, temp.toString());
		  int output = (int) obj.path(str).asDouble();
		  ((ObjectNode) obj).put(str, output);
//		  System.out.println(mediaObj.path(str1).path(str2));
	}
	
	private static void ChangedHost(ObjectNode streamsObj) {
		String str = streamsObj.path("host").toString();
		str = str.replace("\"https://", "");
		str = str.replace("\"rtmp://", "");
		str = str.replace("\"http://", "");
		//System.out.println("str = " + str);
		String[] temp;
		temp = str.split("\\.");
//		for (int i = 0; i < temp.length; i++)
//			System.out.println(temp[i]);
		String[] host_str = new String[temp.length];
		for (int i = 0; i < host_str.length; i++)
			host_str[i] = "";
		String result = "";
		int index = 0;
		for(int i = 0; i < temp.length; i++){
			if(temp[i].startsWith("a-"))
				host_str[0] = temp[i];
			else if(temp[i].startsWith("c-"))
				host_str[1] = temp[i];
			else if(temp[i].startsWith("i-"))
				host_str[2] = temp[i];
		}
		for(int i = 3; i < temp.length; i++)
			host_str[i] = temp[i];
		for(int i = 0; i < host_str.length; i++){
			//System.out.println(host_str[i]);
			result += (host_str[i]);
		}
		result = result.replace("rtmp", "http");
		streamsObj.put("host", result);
		//System.out.println("hostresult = " + result);
	}
	
	private static void changeMetaThumbnail(ObjectNode metaObj) {
		if(metaObj.hasNonNull("thumbnail")){
			String[] temp;
			String thumbnail_str = metaObj.path("thumbnail").toString();
			temp = thumbnail_str.split("http:\\/\\/");
			if(temp.length == 2){
				thumbnail_str = temp[temp.length-1];
			}
			else {
				thumbnail_str = temp[temp.length-1];
			}
			metaObj.put("thumbnail", thumbnail_str);
		}
		else
			metaObj.put("thumbnail", "");
	}
	
	private static void changeStreamsPath(ObjectNode streamsObj) {
		String pathStr = streamsObj.path("path").toString();
		//System.out.println(pathStr);
		String[] temp;
		pathStr = pathStr.replace(".mp4", "");
		pathStr = pathStr.replace("\"", "");
		pathStr = pathStr.replace("/", "");
		pathStr = pathStr.replaceAll("&devtype[^&$]*", "");
		pathStr = pathStr.replaceAll("&plrs[^&$]*", "");
		pathStr = pathStr.replaceAll("&x=[^&$]*", "");
		pathStr = pathStr.replaceAll("&s=[^&$]*", "");
		
		temp = pathStr.split("\\?");
		pathStr = temp[0];
		if(temp.length < 2){
			streamsObj.put("path", pathStr);
			return;
		}
		temp = temp[1].split("&");
		String regionStr = "";
		String siteStr = "";
		for(int i = 0; i < temp.length; i++){
			if(temp[i].startsWith("region")){
				regionStr = temp[i];
				temp[i] = "";
			}
			else if(temp[i].startsWith("site")){
				siteStr = temp[i];
				temp[i] = "";
			}
		}
		for(int i = 0; i < temp.length; i++)
			pathStr += temp[i];
		pathStr += regionStr;
		pathStr += siteStr;
		streamsObj.put("path", pathStr);
		//System.out.println(pathStr);
	}
	
	private static void changeMetrics(JsonNode mediaObj) {
		if(mediaObj.path("metrics").isMissingNode()) return;
		ObjectNode metricsObj = (ObjectNode) mediaObj.path("metrics");
		//if(metricsObj.path("plidl").isMissingNode())
			metricsObj.put("plidl", "");
		//if(metricsObj.path("isrc").isMissingNode())
			metricsObj.put("isrc", "");
			metricsObj.put("pspid", "");
			//metricsObj.put("beacons", "");
	}

	
	public static void main(String[] args){
		System.out.println("Testing Begin");
		try {
			
		  String accessLogFolder = "/Users/herizhao/workspace/accessLog/";
//		  String yqlFileName = "json_cmp/test1.log";
//		  String yqlpFileName = "json_cmp/test2.log";
		  String yqlFileName = "tempLog/0812_yql.res";
		  String yqlpFileName = "tempLog/0812_yqlp.res";
	      ReadResults input1 = new ReadResults(accessLogFolder + yqlFileName);
	      ReadResults input2 = new ReadResults(accessLogFolder + yqlpFileName);
	      Integer diffNum = 0;
	      Integer errorCount = 0;
	      Integer totalIDNum1 = 0;
	      Integer totalIDNum2 = 0;
	      Integer equalIDwithDuplicate = 0;
	      Integer beacons = 0;
	      Integer lineNum = 0;
	      Integer tempCount = 0;
	      HashMap<String, IDclass> IDarray = new HashMap<String, IDclass>();
	      FileOutputStream fos=new FileOutputStream("/Users/herizhao/workspace/accessLog/json_cmp/cmp_result.txt");
	      OutputStreamWriter osw=new OutputStreamWriter(fos);
	      BufferedWriter bw=new BufferedWriter(osw);
	      FileOutputStream consoleStream = new FileOutputStream("/Users/herizhao/workspace/accessLog/json_cmp/console");
	      OutputStreamWriter consoleOSW=new OutputStreamWriter(consoleStream);
	      BufferedWriter console=new BufferedWriter(consoleOSW);
	      while(true){
	    	  input1.ReadNextLine();
	    	  if(input1.line == null) break;
		      input2.ReadNextLine();
		      if(input2.line == null) break;
		      while(input1.line.equals("")){
	    		  lineNum++;
		    	  input1.ReadNextLine();
	    		  input2.ReadNextLine();
	    	  }
		      if(input2.line == null) break;
		      if(input1.line == null) break;
		      lineNum++;
		      System.out.println("lineNum = " + lineNum);
		      String str1 = input1.line;
		      String str2 = input2.line;
		      ObjectMapper mapper1 = new ObjectMapper();
		      ObjectMapper mapper2 = new ObjectMapper();
		      JsonNode root1 = mapper1.readTree(str1);
		      JsonNode root2 = mapper2.readTree(str2);
		      JsonNode mediaNode1 = root1.path("query").path("results").path("mediaObj");
		      JsonNode mediaNode2 = root2.path("query").path("results").path("mediaObj");
		      if(mediaNode2.isMissingNode() && !mediaNode1.isMissingNode())
		    	  tempCount += mediaNode1.size();
		      //For yqlp
		      if (mediaNode2.isArray()){
		    	  totalIDNum2 += mediaNode2.size();
		    	  for(int i = 0; i < mediaNode2.size(); i++){
		    		  ObjectNode mediaObj = (ObjectNode) mediaNode2.get(i);
		    		  mediaObj.put("yvap", "");
		    		  JsonNode streamsNode = mediaObj.path("streams");
		    		  //streams
		    		  if (streamsNode.isArray()) {
						for(int j = 0; j < streamsNode.size(); j++){
							ObjectNode streamsObj = (ObjectNode) streamsNode.get(j);
							changeStreamsPath(streamsObj);
				    		ChangedHost(streamsObj);
				    		//if(streamsObj.path("h264_profile").isMissingNode())
				    			streamsObj.put("h264_profile", "");
				    		if(streamsObj.path("is_primary").isMissingNode())
				    			streamsObj.put("is_primary", false);
						}
					  }
		    		  //meta
		    		  if(!mediaObj.path("meta").isMissingNode()){
		    			  ObjectNode metaObj = (ObjectNode) mediaObj.path("meta");
		    			  changeMetaThumbnail(metaObj);
		    			  if(metaObj.path("show_name").isMissingNode())
				    			metaObj.put("show_name", "");
		    			  if(metaObj.path("event_start").isMissingNode())
		    				  metaObj.put("event_start", "");
		    			  if(metaObj.path("event_stop").isMissingNode())
		    				  metaObj.put("event_stop", "");
		    			  //if(metaObj.path("credits").path("label").isMissingNode())
		    				  ((ObjectNode) metaObj.path("credits")).put("label", "");
		    		  }
		    		  //Metrics -> plidl & isrc
		    		  changeMetrics(mediaObj);
		    	  	}
		      }
		      
		      //For yql
		      if (mediaNode1.isArray()){
		    	  totalIDNum1 += mediaNode1.size();
		    	  for(int i = 0; i < mediaNode1.size(); i++){
		    		  JsonNode mediaObj = mediaNode1.get(i);
		    		  ((ObjectNode) mediaObj).put("yvap", "");
		    		  //Meta
		    		  //System.out.println("meta: ");
		    		  if(!mediaObj.path("meta").isMissingNode()){
		    			  ObjectNode metaObj = (ObjectNode) mediaObj.path("meta");
		    			  changeMetaThumbnail(metaObj);
		    			  metaObj.put("event_start", "");
		    			  metaObj.put("event_stop", "");
			    		  FloatingtoInt(metaObj, "duration");
			    		  if(metaObj.path("show_name").isMissingNode())
				    			metaObj.put("show_name", "");
			    		  //System.out.println("thub_dem: ");
			    		  if(!metaObj.path("thumbnail_dimensions").isMissingNode()){
			    			  ObjectNode thub_demObj = (ObjectNode) metaObj.path("thumbnail_dimensions");
				    		  FloatingtoInt(thub_demObj, "height");
				    		  FloatingtoInt(thub_demObj, "width");
			    		  }
			    		  ((ObjectNode) metaObj.path("credits")).put("label", "");
		    		  }
		    		  //Visualseek
		    		  //System.out.println("visualseek: ");
		    		  if(!mediaObj.path("visualseek").isMissingNode()){
		    			  ObjectNode visualseekObj = (ObjectNode) mediaObj.path("visualseek");
			    		  FloatingtoInt(visualseekObj, "frequency");
			    		  FloatingtoInt(visualseekObj, "width");
			    		  FloatingtoInt(visualseekObj, "height");
			    		  //visualseek -> images, float to int
			    		  JsonNode imagesNode = visualseekObj.path("images");
			    		  if(imagesNode.isArray()){
			    			  for(int j = 0; j < imagesNode.size(); j++){
			    				  ObjectNode imageObj = (ObjectNode) imagesNode.get(j);
			    				  FloatingtoInt(imageObj, "start_index");
			    				  FloatingtoInt(imageObj, "count");
			    			  }
			    		  }
		    		  }
		    		  //Streams
		    		  //System.out.println("streams: ");
		    		  JsonNode streamsNode = mediaObj.path("streams");
		    		  if(streamsNode.isArray()){
		    			  for(int j = 0; j < streamsNode.size(); j++){
		    				  ObjectNode streamsObj = (ObjectNode) streamsNode.get(j);
		    				  FloatingtoInt(streamsObj, "height");
				    		  FloatingtoInt(streamsObj, "bitrate");
				    		  FloatingtoInt(streamsObj, "duration");
				    		  FloatingtoInt(streamsObj, "width");
				    		  changeStreamsPath(streamsObj);
				    		  ChangedHost(streamsObj);
//				    		  if(streamsObj.path("h264_profile").isMissingNode())
					    			streamsObj.put("h264_profile", "");
				    		  if(streamsObj.path("is_primary").isMissingNode())
					    			streamsObj.put("is_primary", false);
		    			  }
		    		  }
		    		  //Metrics -> plidl & isrc
	    			  changeMetrics(mediaObj);
		    	  }
		      }
		      
		      //Compare
		      if(mediaNode2.isArray() && mediaNode1.isArray()){
		    	  for(int i = 0; i < mediaNode2.size() && i < mediaNode1.size(); i++){
		    		  JsonNode mediaObj1 = mediaNode1.get(i);
		    		  JsonNode mediaObj2 = mediaNode2.get(i);
		    		  if(!mediaObj1.equals(mediaObj2)){
		    			  if(!mediaObj1.path("id").toString().equals(mediaObj2.path("id").toString())){
		    				  errorCount++;
		    			  }
		    			  else{
		    				  Integer IFdiffStreams = 0;
		    				  Integer IFdiffMeta = 0;
		    				  Integer IFdiffvisualseek = 0;
		    				  Integer IFdiffMetrics = 0;
		    				  Integer IFdifflicense = 0;
		    				  Integer IFdiffclosedcaptions = 0;
		    				  String statusCode = "";
		    				  MetaClass tempMeta = new MetaClass();
		    					if(!mediaObj1.path("status").equals(mediaObj2.path("status"))){
		    						JsonNode statusNode1 = mediaObj1.path("status");
		    						JsonNode statusNode2 = mediaObj2.path("status");
		    						if(statusNode2.path("code").toString().equals("\"100\"") ||
		    						   (statusNode1.path("code").toString().equals("\"400\"") && statusNode1.path("code").toString().equals("\"404\"")) ||
		    						   (statusNode1.path("code").toString().equals("\"200\"") && statusNode1.path("code").toString().equals("\"200\"")) ||
		    						   (statusNode1.path("code").toString().equals("\"200\"") && statusNode1.path("code").toString().equals("\"403\"")))
		    							statusCode = "";
		    						else
		    							statusCode = "yql code: " + mediaObj1.path("status").toString()
		    										 + " yqlp code:" + mediaObj2.path("status").toString();
		    					}
		    					else {//Status code is 100
			    				  if(!mediaObj1.path("streams").equals(mediaObj2.path("streams")))
			    					  IFdiffStreams = 1;
			    				  if(!tempMeta.CompareMeta(mediaObj1.path("meta"), mediaObj2.path("meta"), lineNum))
			    					  IFdiffMeta = 1;
			    				  if(!mediaObj1.path("visualseek").equals(mediaObj2.path("visualseek")))
			    					  IFdiffvisualseek = 1;
			    				  if(!mediaObj1.path("metrics").equals(mediaObj2.path("metrics"))){
			    					  IFdiffMetrics = 1;
			    					  JsonNode metrics1 = mediaObj1.path("metrics");
			    					  JsonNode metrics2 = mediaObj2.path("metrics");
			    					  if(!metrics1.path("beacons").equals(metrics2.path("beacons")))
			    						  beacons++;
			    				  }
			    				  if(!mediaObj1.path("license").equals(mediaObj2.path("license")))
			    					  IFdifflicense = 1;
			    				  if(!mediaObj1.path("closedcaptions").equals(mediaObj2.path("closedcaptions")))
			    					  IFdiffclosedcaptions = 1;
		    				  }  
		    				  if(IFdiffStreams + IFdiffMeta + IFdiffvisualseek + IFdiffMetrics + IFdifflicense + IFdiffclosedcaptions != 0 ||
		    						  !statusCode.equals("")){
		    					  String ID_str = mediaObj1.path("id").toString();
				    			  if(!IDarray.containsKey(ID_str)){
				    				  IDclass temp_IDclass = new IDclass(ID_str);
				    				  temp_IDclass.addNum(IFdiffStreams, IFdiffMeta, IFdiffvisualseek, 
				    						  IFdiffMetrics, IFdifflicense, IFdiffclosedcaptions, lineNum);
				    				  if(!statusCode.equals(""))	temp_IDclass.statusCode = statusCode;
				    				  IDarray.put(ID_str, temp_IDclass);
				    			  }
				    			  else{
				    				  IDarray.get(ID_str).addNum(IFdiffStreams, IFdiffMeta, IFdiffvisualseek, 
				    						  IFdiffMetrics, IFdifflicense, IFdiffclosedcaptions, lineNum);
				    				  if(!statusCode.equals(""))	IDarray.get(ID_str).statusCode = statusCode;
				    			  }
				    			  IDarray.get(ID_str).stream.CompareStream(IFdiffStreams, mediaObj1.path("streams"), mediaObj2.path("streams"), lineNum);
				    			  if(!IDarray.get(ID_str).metaDone){
				    				  IDarray.get(ID_str).meta = tempMeta;
				    				  IDarray.get(ID_str).metaDone = true;
				    			  }
		    				  }
		    				  else equalIDwithDuplicate++;
		    			  }
		    		  }
		    		  else
		    			  equalIDwithDuplicate++;
		    	  }
		      }
		      bw.flush();
		      console.flush();
		      
	      }//while
	      System.out.println("done");
	      bw.write("Different ID" + "	" + "num	");
	      bw.write(PrintStreamsTitle());
	      bw.write(PrintMetaTitle());
	      bw.write(PrintTitle());
	      bw.newLine();
	      Iterator<String> iter = IDarray.keySet().iterator(); 
	      while (iter.hasNext()) { 
	    	  String key = iter.next();
	    	  bw.write(key + "	");
	    	  bw.write(IDarray.get(key).num.toString() + "	");
	    	  bw.write(IDarray.get(key).stream.print());
	    	  bw.write(IDarray.get(key).meta.print());
	    	  bw.write(IDarray.get(key).print());
	    	  bw.newLine();
	    	  //System.out.println(key);
	      }
	      //System.out.println("different log num = " + diffNum);
	      //System.out.println("same log num = " + sameLogNum);
	      System.out.println("Different ID size = " + IDarray.size());
//	      System.out.println("streamEqual = " + streamEqual);
//	      System.out.println("metaEqual = " + metaEqual);
//	      System.out.println("metricsEqual = " + metricsEqual);
//	      System.out.println("visualseekEqual = " + visualseekEqual);
//	      System.out.println("licenseEqual = " + licenseEqual);
//	      System.out.println("closedcaptionsEqualEqual = " + closedcaptionsEqual);
	      System.out.println(tempCount);
	      System.out.println("beacons = " + beacons);
	      System.out.println("equalIDwithDuplicate = " + equalIDwithDuplicate);
	      System.out.println("Total ID num yql (including duplicates) = " + totalIDNum1);
	      System.out.println("Total ID num yqpl (including duplicates) = " + totalIDNum2);
	      System.out.println("Error " + errorCount);
	      bw.close();
	      console.close();
	    }  
	    catch (IOException e)  
	    {  
	    }
	}
	

	public static String PrintStreamsTitle() {
		String output;
		output = "host	path	width	height	duration	minme_type	live	h264_profile	is_primary	lineNumForStream	"
				+ "stream size diff	";
		return output;
	}
	
	public static String PrintMetaTitle() {
		String output;
		output = "attibution	description	title	genre	embed_rights	duration	url	thumbnail	created_date	"
				+ "provider	thumbnail_dimentions	credits	provisioning_source	show_name	age_gate	lineNumForMeta	";
		return output;
	}
	
	public static String PrintTitle() {
		String output = new String();
		output = "number of diff ID" + "	" + "streamsDiffNum" + "	" + 
	    		  "metaDiffNum" + "	" + "visualseekDiffNum" + "	" + "metricsDiffNum" + "	" + 
	    		  "licenseDiffNum" + "	" + "closedcaptionsDiffNum" + "	" + "ErrorlineNum" + 
	    		  "	" + "status code" + "	";
		return output;
	}
}
