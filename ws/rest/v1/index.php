<?php
	require '../Slim/Slim.php';
	\Slim\Slim::registerAutoloader();
	
	$app = new \Slim\Slim();

	/**
	 * include route definitions
	 */
	 
	include "hello.php";
	include "findEvents.php";
		
	$app->run();
?>
	
