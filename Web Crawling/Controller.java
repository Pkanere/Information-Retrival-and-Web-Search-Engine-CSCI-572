package crawler;

import java.util.HashSet;
import java.util.List;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {

 public static void main(String[] args) throws Exception {
  // TODO Auto-generated method stub
  String crawlStorageFolder = "/data/crawl";
  int numberOfCrawlers = 7;

  CrawlConfig config = new CrawlConfig();
  config.setCrawlStorageFolder(crawlStorageFolder);
  config.setMaxDepthOfCrawling(16);
  config.setMaxPagesToFetch(20000);

  crawlstate state = new crawlstate();



  /*          * Instantiate the controller for this crawl.          */
  PageFetcher pageFetcher = new PageFetcher(config);
  RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
  RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
  CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);


  controller.addSeed("https://www.theguardian.com/us");





  /*          * Start the crawl. This is a blocking operation, meaning that your code          * will reach the line after this only when crawling is finished.          */
  controller.start(MyCrawler.class, numberOfCrawlers);
  List < Object > crawlerdata = controller.getCrawlersLocalData();
  for (Object localData: crawlerdata) {
   crawlstate cs = (crawlstate) localData;
   state.attemptUrls.addAll(cs.attemptUrls);
   state.discovered.addAll(cs.discovered);
   state.dis_NO.addAll(cs.dis_NO);
   state.visitedUrls.addAll(cs.visitedUrls);

  }
  System.out.println("Stats " + state.attemptUrls.size() + " " + state.discovered.size() + " " + state.dis_NO.size() + " " + state.visitedUrls.size());


  HashSet < String > hashSet = new HashSet < String > ();
  int inUrls = 0;

  for (String s: state.discovered) {
   if (!hashSet.contains(s)) {
    hashSet.add(s);
    inUrls++;

   }
  }
  HashSet < String > hashSet1 = new HashSet < String > ();

  int outUrls = 0;
  for (String s: state.dis_NO) {
   if (!hashSet1.contains(s)) {
    hashSet1.add(s);
    outUrls++;

   }
  }
  System.out.println("# unique URLs within extracted: " + inUrls + "\n");
  System.out.println("# unique URLs out School: " + outUrls + "\n");

 }

}