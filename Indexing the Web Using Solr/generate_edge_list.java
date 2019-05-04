package solr;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class generate_edge_list{
 public static void main(String[] args) throws Exception { //creates filetoURL map
  File file = new File("C:\\Users\\LENOVO\\Downloads\\IRHW3\\testfile1.txt");
  Scanner sc = new Scanner(file);
  String k, v;
  HashMap < String, String > filetoURL = new HashMap < String, String > ();

  while (sc.hasNextLine()) {
   k = sc.nextLine();
   v = sc.nextLine();
   k = k.replace("\n", "").replace("\r", "");
   k = k.trim();
   v = v.replace("\n", "").replace("\r", "");
   v = v.trim();
   filetoURL.put(k, v);
   //System.out.println(k+"---->"+v);
  }
  System.out.println(filetoURL.size());
  sc.close();

  //creates URLtofile Map
  File file1 = new File("C:\\Users\\LENOVO\\Downloads\\IRHW3\\testfile2.txt");
  Scanner sc1 = new Scanner(file1);
  //String k,v;
  HashMap < String, String > URLtofile = new HashMap < String, String > ();

  while (sc1.hasNextLine()) {
   k = sc1.nextLine();
   v = sc1.nextLine();
   k = k.replace("\n", "").replace("\r", "");
   k = k.trim();
   v = v.replace("\n", "").replace("\r", "");
   v = v.trim();
   URLtofile.put(k, v);
   //System.out.println(k+"---->"+v);
  }
  System.out.println(URLtofile.size());
  sc1.close();

  //create edgelist file
  BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\LENOVO\\Downloads\\edgelist6.txt"));

  //generate edgelist
  File dir = new File("C:\\Users\\LENOVO\\Downloads\\guardiannews\\guardiannews");
  Set < String > edges = new HashSet < String > ();
  int count = 0;
  for (File input: dir.listFiles()) {

   System.out.println(count);
   count = count + 1;
   Document document = Jsoup.parse(input, "UTF-8", filetoURL.get(input.getName()));
   Elements links = document.select("a[href]");

   for (Element link: links) {
    String url = link.attr("abs:href").trim();
    if (URLtofile.containsKey(url)) {
     edges.add(input.getName() + " " + URLtofile.get(url) + System.lineSeparator());
    }
   }


  }
  System.out.println(count);
  for (String s: edges) {
   writer.write(s);

  }
  writer.flush();
  writer.close();
  System.out.println("don");
 }

}