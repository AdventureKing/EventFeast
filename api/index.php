<?php
	require 'Slim/Slim.php';
	\Slim\Slim::registerAutoloader();
	
	$app = new \Slim\Slim();

	/**
	 * include route definitions
	 */
	
	$CURR_VERSION = "v1";
	
	include "{$CURR_VERSION}/hello.php";
	include "{$CURR_VERSION}/findEvents.php";
		
	$app->run();

?>
	
