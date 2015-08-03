<?php
	//Environment
	require_once($projectDir.'Class/envSettings.php');

	//Public Functions

	// Classes
	require_once($projectDir.'Class/db_parse/CustomParseUser.php');
	require_once($projectDir.'Class/db_parse/DbHandler.php');
	require_once($projectDir.'Class/db_mysql/DbHandler.php');
	require_once($projectDir.'Class/db_mark/DbHandler.php');
	require_once($projectDir.'Class/db_neo4j/DbHandler.php');
	require_once($projectDir.'Class/userAuth.php');
	require_once($projectDir.'Class/geoFunctions.php');
	require_once($projectDir.'Class/externalAPIs/stubhub/functions.php');
	require_once($projectDir.'Class/externalAPIs/ticketmaster/functions.php');

	// Models
	require_once($projectDir.'Models/findEventsModel.php');
	require_once($projectDir.'Models/friendships.php');
	require_once($projectDir.'Models/followers.php');
	require_once($projectDir.'Models/friends.php');
?>