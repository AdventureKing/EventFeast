<?php

	require_once($_SERVER['DOCUMENT_ROOT'].'/vendor/autoload.php');

	use Neoxygen\NeoClient\ClientBuilder;

	$neoUser = "neo4j";
	$neoPass = "tboys!@#";
	$neoClient = ClientBuilder::create()
  		->addConnection('default', 'http', 'turtleboys.com', 7474, true, $neoUser, $neoPass)
  		->setAutoFormatResponse(true)
  		->build();	

?>