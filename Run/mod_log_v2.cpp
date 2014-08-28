#include <iostream>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <sstream>
#include <getopt.h>
#include <string>
#include <vector>
#include <fstream>
using namespace std;

const string str_where = "where%20";
const string str_and = "and%20";
const string str_comma = "%2c";
const string str_equal = "%3d";
const string str_space = "%20";
const string str_double_quot = "%22";
const string str_single_quot = "%27";
const int character_num = 1024;
stringstream os;
ofstream error("error.log");
int num_resize = 0;
int num_ss = 0;
int num_bw = 0;
int num_statuscode = 0;

void tolowercase(string &str);
void get_params(char *p, vector<string> &list, string &str_id);

int main(int argc, char **argv)
{
	string sleepTime;
	string path;
	bool if_change_path = false;
	bool ifSleep = false;
	string outputFileName;
	bool ofName = false;
	string hostname = "video.media.yql.yahoo.com";

	//getopt
	struct option longOpts[] = {
		{"help", no_argument, NULL, 'h'},
		{"sleep", required_argument, NULL, 's'},
		{"path", required_argument, NULL, 'p'},
		{"outputFileName", required_argument, NULL, 'o'},
		{"hostname", required_argument, NULL, 'n'}
	};
	int opt = 0, index= 0;
	while((opt = getopt_long(argc, argv, "hs:p:o:n:", longOpts, &index)) != -1)
	{
		switch(opt)
		{
			case 'h':
				cout << "Help:" << "\n";
				cout << "--sleep [sleep time in second] --path [new path(if needed)] < [LogFile]" << endl;
				exit(0);
				break;
			case 's':
				ifSleep = true;
				sleepTime.assign(optarg);
				break;
			case 'p':
				path.assign(optarg);
				if_change_path = true;
				break;
			case 'o':
				ofName = true;
				outputFileName.assign(optarg);
				break;
			case 'n':
				hostname.assign(optarg);
				break;
		}
	}
	
	if(!ofName)
	{
		cerr << "plz type echo out FileName" << endl;
		exit(0);
	}


	string sleepCommand = "sleep " + sleepTime;

	os << "rm " + outputFileName << endl;
	//int lineNum = 0;
	while(!cin.eof())
	{
		//cout << ++lineNum << endl;
		vector<string> paramList;
		string str_id;
		string url;
		cin >> url;
		string YahooRemoteIP;
		cin >> YahooRemoteIP;
		string User_Agent;
		getline(cin, User_Agent);

		if(url[0] == '/')
		{
			tolowercase(url);
			//cout << "url = " << url << endl;
			size_t p_where = url.find(str_where);
			size_t p_and = url.find(str_and);
			char *ptr = &url[p_where];
			url[p_and - str_space.length()] = 0;
			ptr = ptr + str_where.length();
			//cout << ptr << endl;
			get_params(ptr, paramList, str_id);

			size_t p_temp;
			while(1)
			{
				p_temp = p_and;
				p_and = url.find(str_and, p_temp+1);
				if(p_and == string::npos)
				{
					p_and = url.find("&", p_temp+1);
					ptr = &url[p_temp];
					//cout << "url[p_and - str_space.length()] = " << url << endl;
					if(url[p_and-1] != '2')
						url[p_and - str_space.length()] = 0;
					else
						url[p_and] = 0;
					ptr = ptr + str_and.length();
					//cout << ptr << endl;
					get_params(ptr, paramList, str_id);
					break;
				}
				ptr = &url[p_temp];
				url[p_and - str_space.length()] = 0;
				ptr = ptr + str_and.length();
				//cout << ptr << endl;
				get_params(ptr, paramList, str_id);
			}
			//Print
			os << "curl -G \"http://" + hostname + ":4080/v1/video/sapi/streams/"
					 + str_id +  '?';
			unsigned int i;
			if(paramList.size() > 0)
			{
				for(i = 0; i < paramList.size()-1; i++)
					os << paramList[i] + '&';
				os << paramList[i];
			}
			//os << "diagnostics=true";
			size_t p_quot1 = User_Agent.find("\"");
			if(p_quot1 != string::npos)
			{
				size_t p_quot2 = User_Agent.find("\"", p_quot1+1);
				User_Agent = User_Agent.substr(p_quot1+1, p_quot2-p_quot1-1);
			}
			if(User_Agent == "	")
				os << "\" -H \"YahooRemoteIP: " + YahooRemoteIP +
				      "\" -H \"Host:video.media.yql.yahoo.com\"" + " >> " + outputFileName << endl;
			else
				os << "\" -H \"YahooRemoteIP: " + YahooRemoteIP + "\" -H \"User-Agent: " + User_Agent +
					  "\" -H \"Host:video.media.yql.yahoo.com\"" + " >> " + outputFileName << endl;
			os << "echo \"\" >> " + outputFileName << endl;
			if(ifSleep)
				os << sleepCommand << endl;
		}
	}
	cout << os.str();
	cout.flush();
	error << "ss: " << num_ss << endl;
	error << "bw: " << num_bw << endl;
	error << "status.code: " << num_statuscode << endl;
	return 0;
}

void get_params(char *p, vector<string> &list, string &str_id)
{
    char buf[character_num];
    //cout << "ptr = " << p << endl;
    sscanf(p, "%[^%%]", buf);
    //cout << "buf = " << buf << endl;
    string temp;
    string result;
	if(!strcmp(buf, "lang") ||
	   !strcmp(buf, "format") ||
	   !strcmp(buf, "protocol") ||
	   !strcmp(buf, "rt") ||
	   !strcmp(buf, "plrs") ||
	   !strcmp(buf, "acctid") ||
	   !strcmp(buf, "plidl") ||
	   !strcmp(buf, "pspid") ||
	   !strcmp(buf, "offnetwork") ||
	   !strcmp(buf, "site") ||
	   !strcmp(buf, "region") ||
	   !strcmp(buf, "override") ||
	   !strcmp(buf, "plist") ||
	   !strcmp(buf, "image_sizes") ||
	   !strcmp(buf, "lmsid") ||
	   !strcmp(buf, "duration") ||
	   !strcmp(buf, "synd") ||
	   !strcmp(buf, "hlspre") ||
	   !strcmp(buf, "resize") ||
	   !strcmp(buf, "width") ||
	   !strcmp(buf, "height"))
	{
		result.assign(buf);
		p = p + result.length() + str_equal.length();
		result.append("=");
		//cout << "p = " << p << endl;
		while(1)
		{
			sscanf(p, "%%%[^%%]", buf);
			//cout << "buf = " << buf << endl;
			temp.assign(buf);
			p = p + temp.length() + 1;
			//cout << "p = " << p << endl;
			temp.erase(0, 2);
			result += temp;
			if(temp == "\0") break;
			result.append(",");
		}
		if(result[result.length()-1] == ',')
			result.erase(result.length()-1);
		//cout << result << endl;
		list.push_back(result);
	}
	else if(!strcmp(buf, "devtype"))
	{
		result = "dev_type";
		p = p + result.length() + str_equal.length();
		result.append("=");
		//cout << "p = " << p << endl;
		while(1)
		{
			sscanf(p, "%%%[^%%]", buf);
			//cout << "buf = " << buf << endl;
			temp.assign(buf);
			p = p + temp.length() + 1;
			//cout << "p = " << p << endl;
			temp.erase(0, 2);
			result += temp;
			if(temp == "\0") break;
			result.append(",");
		}
		if(result[result.length()-1] == ',')
			result.erase(result.length()-1);
		//cout << result << endl;
		list.push_back(result);
	}
	else if(!strcmp(buf, "id") || !strcmp(buf, "ID"))
	{
		string temp;
		temp.assign(p);
		//cout << temp << endl;
		if(temp.find(str_equal) != string::npos)
			//find "=" in ID string
		{
			str_id = temp.substr(8, 36);
			//cout << "singleID = " << str_id << endl;
		}
		else
		{
			char *ptr;
			size_t p1, p2;
			p2 = 0;
			while(1)
			{
				p1 = temp.find(str_double_quot, p2);
				if(p1 == string::npos)
					p1 = temp.find(str_single_quot, p2);
				p2 = temp.find(str_double_quot, p1+1);
				if(p2 == string::npos)
					p2 = temp.find(str_single_quot, p1+1);
				temp[p2] = 0;
				p2++;
				p1 = p1 + str_double_quot.length();
				ptr = &temp[p1];
				//cout << ptr << endl;
				if(temp.find(str_double_quot, p2) == string::npos &&
				   temp.find(str_single_quot, p2))
				{
					str_id.append(ptr);
					break;
				}
				str_id.append(ptr).append(",");
			}
			//cout << str_id << endl;
		}
	}
	else
	{
		if(strcmp(buf, "ssl"))
		{
			if(!strcmp(buf, "ss"))
				num_ss++;
			else if(!strcmp(buf, "bw"))
				num_bw++;
			else if(!strcmp(buf, "status.code"))
				num_statuscode++;
			else
			{
				error << buf << endl;
			}
		}
	}
}


void tolowercase(string &str)
{
	// for(unsigned int i = 0; i < str.size(); i++)
	// 	str[i] = char(tolower(str[i]));
	size_t p_where = str.find("WHERE");
	if(p_where != string::npos)
		str.replace(p_where, 5, "where");
	size_t p_and = str.find("AND");
	while(p_and != string::npos)
	{
		str.replace(p_and, 3, "and");
		p_and = str.find("AND", p_and + 1);
	}
	size_t p_per = str.find('%');
	while(p_per != string::npos)
	{
		str[p_per+2] = char(tolower(str[p_per+2]));
		p_per = str.find('%', p_per + 1);
	}


	size_t p = str.find('+');
	while(p != string::npos)
	{
		str.replace(p, 1, "%20");
		p = str.find('+', p+1);
	}
	p = str.find('=');
	while(p != string::npos)
	{
		str.replace(p, 1, "%3d");
		p = str.find('=', p+1);
	}
	p = str.find('\'');
	while(p != string::npos)
	{
		str.replace(p, 1, "%22");
		p = str.find('\'');
	}
	p = str.find("%27");
	while(p != string::npos)
	{
		str.replace(p, 3, "%22");
		p = str.find("%27", p+1);
	}
}










