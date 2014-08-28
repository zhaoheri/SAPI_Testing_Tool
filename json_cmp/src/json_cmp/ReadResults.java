package json_cmp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class ReadResults{
    InputStream fis;
    BufferedReader br;
    String         line;
    String filename;

    public ReadResults(String filename) throws IOException {
        fis = new FileInputStream(filename);
        br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
        this.filename = filename;
    }

    public void ReadNextLine() throws IOException {
        if((line = br.readLine()) == null){
            System.out.println("EOF: " + this.filename);
        }
    }
}
