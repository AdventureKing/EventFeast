<?php
	ini_set('display_errors',1);
	ini_set('display_startup_errors',1);
	error_reporting(-1);

	$base_path = $_SERVER['DOCUMENT_ROOT']."/";
	$projectDir = $base_path."v1/";   //define the directory containing the project files

	require $base_path.'libs/Slim/Slim.php';
	Slim\Slim::registerAutoloader();
	
	$app = new \Slim\Slim();

	require_once($projectDir.'includes.php');
	require_once($projectDir.'routes.php');

	// User id from db - Global Variable
	$user_id = NULL;
		
	$app->run();
?>
	
