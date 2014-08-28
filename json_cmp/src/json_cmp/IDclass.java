package json_cmp;

public class IDclass {
	/*These XXXDiffNum are for recording how many difference for every object
	 */
	
	String ID;
	Integer num;
	Integer streamsDiffNum;
	Integer metaDiffNum;
	Integer visualseekDiffNum;
	Integer metricsDiffNum;
	Integer licenseDiffNum;
	Integer closedcaptionsDiffNum;
	Integer lineNum;
	String statusCode;
	MetaClass meta;
	StreamClass stream;
	boolean metaDone;
	
	public IDclass(String ID_str){
		this.ID = ID_str;
		this.num = 0;
		this.streamsDiffNum = 0;
		this.metaDiffNum = 0;
		this.visualseekDiffNum = 0;
		this.metricsDiffNum = 0;
		this.licenseDiffNum = 0;
		this.closedcaptionsDiffNum = 0;
		this.lineNum = 0;
		this.statusCode = "";
		metaDone = false;
		meta = new MetaClass();
		stream = new StreamClass();
	}
	
	public void addNum(Integer IFdiffStreams, Integer IFdiffMeta, 
			Integer IFdiffvisualseek, Integer IFdiffMetrics, 
			Integer IFdifflicense, Integer IFdiffclosedcaptions, Integer line){
		num++;
		streamsDiffNum += IFdiffStreams;
		metaDiffNum += IFdiffMeta;
		visualseekDiffNum += IFdiffvisualseek;
		metricsDiffNum += IFdiffMetrics;
		licenseDiffNum += IFdifflicense;
		closedcaptionsDiffNum += IFdiffclosedcaptions;
		this.lineNum = line*2;
	}
	
	public String print() {
		String output = new String();
		output = num + "	" + streamsDiffNum + "	" + metaDiffNum + 
		  "	" + visualseekDiffNum + "	" + metricsDiffNum + "	" + 
		  licenseDiffNum + "	" + closedcaptionsDiffNum 
		  + "	" + lineNum + "	" + statusCode;
		return output;
	}
}















