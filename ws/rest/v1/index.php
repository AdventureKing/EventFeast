<?php
	$base_path = $_SERVER['DOCUMENT_ROOT']."/dev/ws/rest/";
	$projectDir = $base_path."v1/";   //define the directory containing the project files

	require $base_path.'Slim/Slim.php';
	\Slim\Slim::registerAutoloader();
	
	$app = new \Slim\Slim();

	/**
	 * include route definitions
	 */
	 
	require_once($projectDir.'includes.php');
	require_once($projectDir.'routes.php');
		
	$app->run();
?>
	
