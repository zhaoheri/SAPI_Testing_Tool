package json_cmp;

import com.fasterxml.jackson.databind.JsonNode;

public class StreamClass {
	// 9 elements
	String host;
	String path;
	String width;
	String height;
	String duration;
	String mime_type;
	String live;
	String h264_profile;
	String is_primary;
	Integer lineNum;
	Integer sizeDiffNum;
	boolean done;
	
	public StreamClass() {
		host = "";
		path = "";
		width = "";
		height = "";
		duration = "";
		mime_type = "";
		live = "";
		h264_profile = "";
		is_primary = "";
		lineNum = 0;
		sizeDiffNum = 0;
		done = false;
	}

	public void CompareStream(Integer iFdiffStreams, JsonNode streamNode1,
			JsonNode streamNode2, Integer line) {
		if(done) return;
		if(iFdiffStreams == 1){
			done = true;
			if(streamNode1.isArray() && streamNode2.isArray()){
				JsonNode streamObj1;
				JsonNode streamObj2;
				if(streamNode1.size() != streamNode2.size()) {
					sizeDiffNum++;
					done = false;
					streamNode1 = streamNode2;
					return;
				}
				for(int i = 0; i < streamNode1.size() && i < streamNode2.size(); i++){
					streamObj1 = streamNode1.get(i);
					streamObj2 = streamNode2.get(i);
					if(!streamObj1.toString().equals(streamObj2.toString())){
						if(!streamObj1.path("host").toString().equals(streamObj2.path("host").toString()))
							host = streamObj1.path("host").toString() + " " + streamObj2.path("host").toString();
						if(!streamObj1.path("path").toString().equals(streamObj2.path("path").toString())){
							path = streamObj1.path("path").toString() + " " + streamObj2.path("path").toString();
						}
						if(!streamObj1.path("width").toString().equals(streamObj2.path("width").toString())){
							width = streamObj1.path("width").toString() + " " + streamObj2.path("width").toString();
						}
						if(!streamObj1.path("height").toString().equals(streamObj2.path("height").toString()))
							height = streamObj1.path("height").toString() + " " + streamObj2.path("height").toString();
						if(!streamObj1.path("duration").toString().equals(streamObj2.path("duration").toString()))
							duration = streamObj1.path("duration").toString() + " " + streamObj2.path("duration").toString();
						if(!streamObj1.path("mime_type").toString().equals(streamObj2.path("mime_type").toString()))
							mime_type = streamObj1.path("mime_type").toString() + " " + streamObj2.path("mime_type").toString();
						if(!streamObj1.path("live").toString().equals(streamObj2.path("live").toString()))
							live = streamObj1.path("live").toString() + " " + streamObj2.path("live").toString();
						if(!streamObj1.path("h264_profile").toString().equals(streamObj2.path("h264_profile").toString()))
							h264_profile = streamObj1.path("h264_profile").toString() + " " + streamObj2.path("h264_profile").toString();
						if(!streamObj1.path("is_primary").toString().equals(streamObj2.path("is_primary").toString()))
							is_primary = streamObj1.path("is_primary").toString() + " " + streamObj2.path("is_primary").toString();
						lineNum = line*2;
						break;
					}
				}
			}
		}
	}
	
	public String print() {
		String output = new String();
		output = "host: " + host + "	path:" + path + "	width:" + width + "	height:" + height + "	duration:" + 
				 duration + "	mime_type: " + mime_type + "	live: " + live + "	h264_profile: " + h264_profile + "	is_primary: " + 
				 is_primary + "	lineNum: " + lineNum + "	";
		output += sizeDiffNum + "	";
		return output;
	}
}
