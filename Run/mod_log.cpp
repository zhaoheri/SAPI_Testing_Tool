#include <iostream>
#include <stdlib.h>
#include <sstream>
#include <getopt.h>
using namespace std;

int main(int argc, char **argv)
{
	string sleepTime;
	string path;
	bool if_change_path = false;
	bool ifSleep = false;
	string outputFileName;
	bool ofName = false;
	string hostname = "video.query.yahoo.com";

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
				outputFileName.assign(optarg);
				ofName = true;
				break;
			case 'n':
				if(optarg != NULL)
					hostname.assign(optarg);
				break;
		}
	}

	if(!ofName)
	{
		cerr << "plz type echo out FileName:" << endl;
		exit(0);
	}
	string sleepCommand = "sleep " + sleepTime;

	//cout << "sleep time = " << sleepTime << endl;
	stringstream strStream;
	strStream << "rm " + outputFileName << endl;
	while(!cin.eof())
	{
		string url;
		cin >> url;
		if(if_change_path)
		{
			size_t position = url.find("?");
			url.replace(0, position, path);
		}
		size_t p_callback = url.find("&callback");
		if(p_callback != string::npos)
			url.erase(p_callback, url.length()-p_callback);
		size_t format_pos = url.find("format=xml");
		if(format_pos != string::npos)
			url.replace(format_pos, 10, "format=json");
		size_t SELECT_pos = url.find("SELECT");
		size_t FROM_pos = url.find("FROM");
		if(SELECT_pos != string::npos && FROM_pos != string::npos)
			url.replace(SELECT_pos+6, FROM_pos - SELECT_pos - 6, "%20*%20");
		string YahooRemoteIP;
		cin >> YahooRemoteIP;
		string User_Agent;
		getline(cin, User_Agent);
		if(url[0] == '/')
		{
			string temp;
			//cout << "url = " << url << endl;
			//cout << "YahooRemoteIP = " << YahooRemoteIP << endl;
			size_t p_quot1 = User_Agent.find("\"");
			if(p_quot1 != string::npos)
			{
				size_t p_quot2 = User_Agent.find("\"", p_quot1+1);
				User_Agent = User_Agent.substr(p_quot1+1, p_quot2-p_quot1-1);
			}
			if(User_Agent == "	")
				temp = "curl -G \"http://" + hostname + url + "\"" + " -H \"YahooRemoteIP: " +
				YahooRemoteIP + "\" " + "-H \"Host:video.query.yahoo.com\"" + " >> " + outputFileName;
			else
				temp = "curl -G \"http://" + hostname + url + "\"" + " -H \"YahooRemoteIP: " +
						YahooRemoteIP + "\" -H \"User-Agent: " + User_Agent + "\" " + "-H \"Host:video.query.yahoo.com\"" + " >> " + outputFileName;
			strStream << temp << endl;
			strStream << "echo \"\" >> " + outputFileName << endl;
			if(ifSleep)
				strStream << sleepCommand << endl;
		}
	}
	cout << strStream.str();
	cout.flush();
	return 0;
}
