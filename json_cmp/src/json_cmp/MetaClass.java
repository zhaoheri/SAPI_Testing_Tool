package json_cmp;

import java.awt.List;
import java.awt.image.TileObserver;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

public class MetaClass {
	DetailStruct attribution = new DetailStruct();
	DetailStruct description = new DetailStruct();
	DetailStruct title = new DetailStruct();
	DetailStruct genre = new DetailStruct();
	DetailStruct embed_rights = new DetailStruct();
	DetailStruct duration = new DetailStruct();
	DetailStruct url = new DetailStruct();
	DetailStruct thumbnail = new DetailStruct();
	DetailStruct create_date = new DetailStruct();
	DetailStruct provider = new DetailStruct();
	DetailStruct thumbnail_dimensions = new DetailStruct();
	DetailStruct credits = new DetailStruct();
	DetailStruct provisioning_source = new DetailStruct();
	DetailStruct show_name = new DetailStruct();
	DetailStruct age_gate = new DetailStruct();
	Integer lineNum;
	
	public MetaClass() {
		lineNum = 0;
	}
	
	
	
	public String print(){
		String output = "";
		output = "attribution: " + attribution.diff + 
				"	description: " + description.diff + 
				"	title: " + title.diff + 
				"	genre: " + genre.diff + 
				"	embed_right: " + embed_rights.diff + 
				"	duration: " + duration.diff + 
				"	url: " + url.diff + 
				"	thumbnail: " + thumbnail.diff + 
				"	create_date: " + create_date.diff + 
				"	provider: " + provider.diff+ 
				"	thumbnail_dimensions:" + thumbnail_dimensions.diff + 
				"	credits: " + credits.diff + 
				"	provisioning_source: " + provisioning_source.diff + 
				"	show_name:" + show_name.diff + 
				"	age_gate:" + age_gate.diff + 
				"	lineNum:" + lineNum + "	";
		return output;
	}



	public boolean CompareMeta(JsonNode metaNode1, JsonNode metaNode2,
			Integer line) {
		if(metaNode1.equals(metaNode2))
			return true;
		else {
			boolean equal = true;
			if(!metaNode1.path("attribution").equals(metaNode2.path("attribution"))){
				equal = false;
				attribution.num++;
				attribution.diff = metaNode1.path("attribution").toString() + " " + metaNode2.path("attribution").toString();
			}
			if(!metaNode1.path("description").equals(metaNode2.path("description"))){
				equal = false;
				description.num++;
				description.diff = metaNode1.path("description").toString() + " " + metaNode2.path("description").toString();
			}
			if(!metaNode1.path("title").toString().equals(metaNode2.path("title").toString())){
				equal = false;
				title.num++;
				title.diff = metaNode1.path("title").toString() + " " + metaNode2.path("title").toString();
			}
			if(!metaNode1.path("genre").toString().equals(metaNode2.path("genre").toString())){
				equal = false;
				genre.num++;
				genre.diff = metaNode1.path("genre").toString() + " " + metaNode2.path("genre").toString();
			}
			if(!metaNode1.path("embed_rights").toString().equals(metaNode2.path("embed_rights").toString())){
				equal = false;
				embed_rights.num++;
				embed_rights.diff = metaNode1.path("embed_rights").toString() + " " + metaNode2.path("embed_rights").toString();
			}
			if(!metaNode1.path("duration").toString().equals(metaNode2.path("duration").toString())){
				equal = false;
				duration.num++;
				duration.diff = metaNode1.path("duration").toString() + " " + metaNode2.path("duration").toString();
			}
			if(!metaNode1.path("url").toString().equals(metaNode2.path("url").toString())){
				equal = false;
				url.num++;
				url.diff = metaNode1.path("url").toString() + " " + metaNode2.path("url").toString();
			}
			if(!metaNode1.path("thumbnail").toString().equals(metaNode2.path("thumbnail").toString())){
//				System.out.println(metaNode1.path("thumbnail").toString());
//				System.out.println(metaNode2.path("thumbnail").toString());
				String[] thum_str1 = metaNode1.path("thumbnail").toString().split("\\/");
				String[] thum_str2 = metaNode2.path("thumbnail").toString().split("\\/");
				boolean equalThumb = true;
				int ptr1 = 0;
				int ptr2 = 0;
				boolean done = false;
				while(!done){
					for(int i = 0; i < thum_str2.length; i++){
						if(thum_str1[ptr1].equals(thum_str2[i])){
							done = true;
							ptr2 = i;
							break;
						}
					}
					if(!done && ptr1 < thum_str1.length)
						ptr1++;
					if(ptr1 == thum_str1.length-1) break;
				}
				if(done) {
					if(thum_str1.length-ptr1 == thum_str2.length-ptr2){
						for(; ptr1 < thum_str1.length;){
							if(!thum_str1[ptr1++].equals(thum_str2[ptr2++])){
								equalThumb = false;
								break;
							}
						}
					}
					else equalThumb = false;
				}
				else equalThumb = false;
				if(!equalThumb){
					equal = false;
					thumbnail.num++;
					thumbnail.diff = metaNode1.path("thumbnail").toString() + " " + metaNode2.path("thumbnail").toString();
				}
			}
			if(!metaNode1.path("create_date").toString().equals(metaNode2.path("create_date").toString())){
				equal = false;
				create_date.num++;
				create_date.diff = metaNode1.path("create_date").toString() + " " + metaNode2.path("create_date").toString();
			}
			if(!metaNode1.path("provisioning_source").toString().equals(metaNode2.path("provisioning_source").toString())){
				equal = false;
				provisioning_source.num++;
				provisioning_source.diff = metaNode1.path("provisioning_source").toString() + " " + metaNode2.path("provisioning_source").toString();
			}
			if(!metaNode1.path("show_name").toString().equals(metaNode2.path("show_name").toString())){
				equal = false;
				show_name.num++;
				show_name.diff = metaNode1.path("show_name").toString() + " " + metaNode2.path("show_name").toString();
			}
			if(!metaNode1.path("age_gate").toString().equals(metaNode2.path("age_gate").toString())){
				equal = false;
				age_gate.num++;
				age_gate.diff = metaNode1.path("age_gate").toString() + " " + metaNode2.path("age_gate").toString();
			}
			if(!metaNode1.path("provider").toString().equals(metaNode2.path("provider").toString())){
				equal = false;
				provider.num++;
				provider.diff = metaNode1.path("provider").toString() + " " + metaNode2.path("provider").toString();
			}
			if(!metaNode1.path("thumbnail_dimensions").equals(metaNode2.path("thumbnail_dimensions"))){
				equal = false;
				thumbnail_dimensions.num++;
				thumbnail_dimensions.diff = metaNode1.path("thumbnail_dimensions").toString() + " " + metaNode2.path("thumbnail_dimensions").toString();
			}
			if(!metaNode1.path("credits").equals(metaNode2.path("credits"))){
				equal = false;
				credits.num++;
				credits.diff = metaNode1.path("credits").toString() + " " + metaNode2.path("credits").toString();
			}
			lineNum = line*2;
			return equal;
		}
	}
}








