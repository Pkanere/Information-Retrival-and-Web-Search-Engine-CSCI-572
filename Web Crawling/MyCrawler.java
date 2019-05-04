package crawler;

import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {

 private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|rss" +
  "|wav|avi|mov|mpeg|mpg|ram|m4v|wma|wmv|mid|txt" + "|mp2|mp3|mp4|zip|rar|gz|exe))$");
 crawlstate state = new crawlstate();

 /**      * This method receives two parameters. The first parameter is the page      * in which we have discovered this new url and the second parameter is      * the new url. You should implement this function to specify whether      * the given url should be crawled or not (based on your crawling logic).      * In this example, we are instructing the crawler to ignore urls that      * have css, js, git, ... extensions and to only accept urls that start      * with "http://www.viterbi.usc.edu/". In this case, we didn't need the      * referringPage parameter to make the decision.      */

 public Object getMyLocalData() {
  return state;
 }
 @Override
 protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
  String url = webUrl.getURL();
  state.attemptUrls.add(url);
  System.out.println("" + url + " " + statusCode);
 }



 public boolean shouldVisit(Page referringPage, WebURL url) {
  String href = url.getURL().toLowerCase();

  if ((href.startsWith("https://www.theguardian.com/us")) || (href.startsWith("https://theguardian.com/us")) || (href.startsWith("http://www.theguardian.com/us")) || (href.startsWith("http://theguardian.com/us"))) {
   state.discovered.add(href);
   //System.out.println("Discovered:" + url + "OK");
   //Page page = new Page(url);
   //String contentType = page.getContentType().split(";")[0];
   //System.out.println("Type"+contentType);
  } else {
   state.dis_NO.add(href);
   //System.out.println("Discovered:" + url + "N_OK");
  }

  return (!FILTERS.matcher(href).matches()) && ((href.startsWith("https://www.theguardian.com/us")) || (href.startsWith("https://theguardian.com/us")) || (href.startsWith("http://www.theguardian.com/us")) || (href.startsWith("http://theguardian.com/us")));
 }

 public void visit(Page page) {
  String url = page.getWebURL().getURL();
  state.visitedUrls.add(url);
  String contentType = page.getContentType().split(";")[0];
  if (contentType.equals("text/html")) {
   if (page.getParseData() instanceof HtmlParseData) {
    HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
    Set < WebURL > links = htmlParseData.getOutgoingUrls();
    System.out.println("URL:" + url + " " + page.getContentData().length + " " + page.getStatusCode() + " " + links.size() + " " + page.getContentType());
   } else {
    System.out.println("URL:" + url + " " + page.getContentData().length + " " + page.getStatusCode() + " 0 " + page.getContentType());
   }
  } else {
   System.out.println("URL:" + url + " " + page.getContentData().length + " " + page.getStatusCode() + " 0 " + page.getContentType());
  }





 }



}