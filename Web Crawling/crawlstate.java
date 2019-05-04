package crawler;
import java.util.ArrayList;

public class crawlstate {


 ArrayList < String > attemptUrls;
 ArrayList < String > discovered;

 ArrayList < String > dis_NO;
 ArrayList < String > visitedUrls;



 public crawlstate() {

  attemptUrls = new ArrayList < String > ();
  discovered = new ArrayList < String > ();
  dis_NO = new ArrayList < String > ();
  visitedUrls = new ArrayList < String > ();



 }



}