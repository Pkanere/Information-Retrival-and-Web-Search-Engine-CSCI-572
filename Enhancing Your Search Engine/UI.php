<?php
ini_set('memory_limit',-1);
//ini_set('max_execution_time', -1);
include 'SpellCorrector.php';
include 'textSnippet.php';
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
    //$sugestion = $solr->suggest($query);
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
 <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<!-- jQuery UI -->
<link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/themes/smoothness/jquery-ui.css">
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js"></script>


  <script>
console.log("Out");
$( function() {
console.log("in");
 // Single Select
 $( "#q" ).autocomplete({
  source: function( request, response ) {
  var query = $("#q").val().toLowerCase();
  var end = query.substring(query.lastIndexOf(" ") + 1);
  var start = query.substring(0,query.lastIndexOf(" "));
   // Fetch data
   $.ajax({
    url: "http://localhost:8983/solr/latest/suggest?q="+end,
    type: 'post',
    dataType: "json",
    data: {
     search: request.term
    },
    success: function( data ) {
     console.log(data.suggest.suggest);
     var suggest = data.suggest.suggest[end].suggestions;
     var values = [];
     var len = suggest.length;
     for(var i=0;i<len ;i++){
     if(start=="")
        {
     	console.log(suggest[i].term);
     	values[i]=suggest[i].term;
	}
	else
	{console.log(start+" "+suggest[i].term);
     	values[i]=start+" "+suggest[i].term;
	
	}

}

     response( values );
    }
   });
  },

 });



});

  </script>
<style>

</style>
</head>
   

  <body>
	<center>
    <form  accept-charset="utf-8" method="get" >
      <label for="q">Search:</label>
      
<input id="q" name="q" type="text" value="<?php echo htmlspecialchars($query, ENT_QUOTES, 'utf-8'); ?>"/><br>
      <input type="radio" name="sort" value="score desc"> Lucene (Default)
      <input type="radio" name="sort" value="prf desc"> Page Rank<br>
      <input type="submit"/>
    </form>
</center>
<?php

// display results
if ($results)
{ //echo $suggestion;
  $total = (int) $results->response->numFound;
  $start = min(1, $total);
  $end = min($limit, $total);
$q=$query;
  $words = explode(" " ,$q);
$correct_term="";
  foreach ($words as $w)
{
$correct_term .= SpellCorrector::correct($w)." ";
}
$correct_term=trim($correct_term);
//  $correct_term = SpellCorrector::correct($query);
  if($correct_term != strtolower($query))
{ $link = "http://localhost/solr-php-client/ui_final.php?q=$correct_term&sort=$sort";
?>
    <div> Did you mean: <a href="<?php echo $link; ?>"><?php echo $correct_term; ?></a></div>
<?php
}
if($total == 0)
{
?>  
    <div>Your search <?php echo $query; ?> did not match any documents</div>

<?php
}
else
{
?> 
    <div>About <?php echo $total; ?> results</div>
    <ol>
<?php
  
  // iterate result documents
  foreach ($results->response->docs as $doc)
  {
$path =  htmlspecialchars($doc->id, ENT_NOQUOTES, 'utf-8');
$filename = substr($path,46,41);  
$file = '/home/pranali/Desktop/solr-8.0.0/newparsed/newparsed/'.$filename;

$f = file_get_contents($file);
$snip = new TextSnippet();
$snip->setMinWords(0);
$snip->setMaxWords(25);
$snip->setHighlightTemplate('<strong>%word%</strong>');
$s = $snip->createSnippet($query, $f);
if($s=='')
{$s = "No Snippets";
}

?>
      <li>
        <table  style="text-align: left;">
         <tr>
	    
            <td><a href="<?php echo htmlspecialchars($doc->og_url, ENT_NOQUOTES, 'utf-8'); ?>" style="text-decoration: none"><font size="5" ><?php echo htmlspecialchars($doc->title, ENT_NOQUOTES, 'utf-8'); ?></font></a></td>	
         </tr>
         <tr>
	    
            <td><a href="<?php echo htmlspecialchars($doc->og_url, ENT_NOQUOTES, 'utf-8'); ?>"  style="text-decoration: none"><font size="3" color="green"><?php echo htmlspecialchars($doc->og_url, ENT_NOQUOTES, 'utf-8'); ?><font size="3" color="red"></a></td>
         </tr>
	
	<tr>
	  
            <td><?php echo htmlspecialchars($doc->id, ENT_NOQUOTES, 'utf-8'); ?></td>
         </tr>

	<tr>
	  
            <td><?php echo $s; ?></td>
         </tr>
	<tr>
       <tr>
	  
            <td><?php echo (" "); ?></td>
         </tr>
	<tr>
<tr>
	  
            <td><?php echo (" "); ?></td>
         </tr>
	<tr>
        </table>
      </li>
<?php
  }
?>
    </ol>
<?php
}
?> 

<?php
}
?>
  </body>
</html>