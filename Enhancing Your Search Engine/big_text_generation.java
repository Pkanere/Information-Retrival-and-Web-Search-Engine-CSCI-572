package solr;
import java.io.BufferedWriter;

import java.io.File;

import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.FileWriter;

import java.io.IOException;

import java.util.ArrayList;

import java.util.Arrays;

import java.util.HashSet;

import java.util.Set;

import org.apache.tika.exception.TikaException;

import org.apache.tika.metadata.Metadata;

import org.apache.tika.parser.ParseContext;

import org.apache.tika.parser.html.HtmlParser;

import org.apache.tika.sax.BodyContentHandler;

import org.xml.sax.SAXException;

import java.util.List;

public class big_text_generation {
	public static void main(String [] args) throws IOException, SAXException, TikaException
	{
	BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\LENOVO\\Downloads\\big2.txt"));
	    
	File dir = new File("C:\\Users\\LENOVO\\Downloads\\guardiannews\\11");
	int count = 0;
	for(File input: dir.listFiles())
	{
	BodyContentHandler handler = new BodyContentHandler(-1);

    Metadata metadata = new Metadata();

    FileInputStream inputstream = new FileInputStream(input);
 
    new HtmlParser().parse(inputstream, handler, metadata, new ParseContext());

    String myString = handler.toString();

    List<String> al = new ArrayList<String>();
	
	al = Arrays.asList(myString.split("\\W+"));
	
	for(String x: al)
	{
	    writer.write(x+"\n");
	}
    count=count+1;
    System.out.println(count);
	}
    
    writer.close();
    System.out.println("done");
    
	}
}
