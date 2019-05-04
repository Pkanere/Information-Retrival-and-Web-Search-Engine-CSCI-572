package solr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class htmlparse {
	public static void main(String [] args) throws IOException
	{   File dir = new File("C:\\Users\\LENOVO\\Downloads\\guardiannews\\parsed");
	int count=0;
	for(File myFile: dir.listFiles())
	{   count = count+1;
		String name = myFile.getName();
		System.out.println(count);
	
		
		BufferedReader br = new BufferedReader(new FileReader(myFile)); 
		  
		String st; 
		String s="";
		while ((st = br.readLine()) != null) 
			if(st.length()>20)

				s=s+st+'.'+' ';
		String path = "C:\\Users\\LENOVO\\Downloads\\guardiannews\\newparsed\\"+name;
		BufferedWriter writer = new BufferedWriter(new FileWriter(path));
		writer.write(s);
		writer.close();
		  
	}
	}

}
