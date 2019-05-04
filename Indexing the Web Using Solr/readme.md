# Solr Based Web Search Engine

- Used the Apache Solr software to import a set of web pages, extract HTML content using Apache TIKA library and index html files
- Created a web page with a text box where user can enter a query. The user’s query will be processed by a program at the web server which formats the query and sends it to Solr. Solr process the query and return some results in JSON format. A program on the web server re-formats the results and present them to the user as any search engine would do. 
- Compared the results of two ranking strategies - default Lucene and Pagerank algorithm
