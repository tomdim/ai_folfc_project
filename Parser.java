package Project2.inference_method_4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
	private BufferedReader reader;
		
	public String parse(String KBpath) throws IOException
	{
		FileReader input = null;
		String tell = "", curLine = null;
		try {
			input = new FileReader(KBpath);
			reader = new BufferedReader(input);
			while ( (curLine = reader.readLine()) != null)  
				tell += curLine + ";";
		} catch (IOException e) {
			e.printStackTrace();
		}
		reader.close();
		return tell;
	}

}
