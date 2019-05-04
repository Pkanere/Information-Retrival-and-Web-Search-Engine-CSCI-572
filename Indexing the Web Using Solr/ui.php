<?php

// make sure browsers see this page as utf-8 encoded HTML
header('Content-Type: text/html; charset=utf-8');

$limit = 10;
$query = isset($_REQUEST['q']) ? $_REQUEST['q'] : false;
$sort = $_REQUEST['sort'];
$results = false;
$additionalParameters = array('fl'=>['title', 'og_url', 'id', 'og_description'], 'sort'=>$sort);
if ($query)
{
  // The Apache Solr Client library should be on the include path
  // which is usually most easily accomplished by placing in the
  // same directory as this script ( . or current directory is a default
  // php include path entry in the php.ini)
  require_once('Apache/Solr/Service.php');

  // create a new solr service instance - host, port, and webapp
  // path (all defaults in this example)
  $solr = new Apache_Solr_Service('localhost', 8983, '/solr/latest/');

  // if magic quotes is enabled then stripslashes will be needed
  if (get_magic_quotes_gpc() == 1)
  {
    $query = stripslashes($query);
  }

  // in production code you'll always want to use a try /catch for any
  // possible exceptions emitted  by searching (i.e. connection
  // problems or a query parsing error)
  try
  {
    $results = $solr->search($query, 0, $limit, $additionalParameters);
  }
  catch (Exception $e)
  {
    // in production you'd probably log or email this error to an admin
    // and then show a special message to the user but for this example
    // we're going to show the full exception
    die("<html><head><title>SEARCH EXCEPTION</title><body><pre>{$e->__toString()}</pre></body></html>");
  }
}

?>
<html>
  <head>
    <title>Search Engine</title>
  </head>
  <body>
    <form  accept-charset="utf-8" method="get">
      <label for="q">Search:</label>
      <input id="q" name="q" type="text" value="<?php echo htmlspecialchars($query, ENT_QUOTES, 'utf-8'); ?>"/><br>
      <input type="radio" name="sort" value="score desc"> Lucene (Default)<br>
      <input type="radio" name="sort" value="pageRankFile desc"> Page Rank<br>
      <input type="submit"/>
    </form>
<?php

// display results
if ($results)
{
  $total = (int) $results->response->numFound;
  $start = min(1, $total);
  $end = min($limit, $total);
?>
    <div>Results <?php echo $start; ?> - <?php echo $end;?> of <?php echo $total; ?>:</div>
    <ol>
<?php
  // iterate result documents
  foreach ($results->response->docs as $doc)
  {
?>
      <li>
        <table width="1000" style="border: 0.5px solid black; text-align: left;">
         <tr>
	    <th>title</th>
            <td><a href="<?php echo htmlspecialchars($doc->og_url, ENT_NOQUOTES, 'utf-8'); ?>"><?php echo htmlspecialchars($doc->title, ENT_NOQUOTES, 'utf-8'); ?></a></td>	
         </tr>
         <tr>
	    <th>og_url</th>
            <td><a href="<?php echo htmlspecialchars($doc->og_url, ENT_NOQUOTES, 'utf-8'); ?>"><?php echo htmlspecialchars($doc->og_url, ENT_NOQUOTES, 'utf-8'); ?></a></td>
         </tr>
	<tr>
	    <th>id</th>
            <th><?php echo htmlspecialchars($doc->id, ENT_NOQUOTES, 'utf-8'); ?></th>	
         </tr>
	<tr>
	    <th>og_description</th>
            <th><?php if($doc->og_description) echo htmlspecialchars($doc->og_description, ENT_NOQUOTES, 'utf-8'); else echo "N/A";?></th>	
         </tr>

        </table>
      </li>
<?php
  }
?>
    </ol>
<?php
}
?>
  </body>
</html>
