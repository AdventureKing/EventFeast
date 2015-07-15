<?php
	// AKA Environment name
	$parentDir = strtoupper(dirname(__FILE__));
	$parentDirArray = explode("/",$parentDir);
	$envName = $parentDirArray[count($parentDirArray)-2]; 
?>

<html>
  <head>
    <title>www.api.turtleboys.com</title>
  </head>
  <body>
    <h1>Turtleboys API (<?php echo $envName; ?>  Environment)</h1>
  </body>
</html>
