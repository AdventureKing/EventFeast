<?php
	//Environment
	require_once($projectDir.'Class/envSettings.php');

	//Public Functions

	// Classes
	require_once($projectDir.'Class/db_parse/CustomParseUser.php');
	require_once($projectDir.'Class/db_parse/DbHandler.php');
	require_once($projectDir.'Class/db_mysql/DbHandler.php');
	require_once($projectDir.'Class/userAuth.php');
	require_once($projectDir.'Class/externalAPIs/stubhub/functions.php');
	require_once($projectDir.'Class/externalAPIs/ticketmaster/functions.php');

	// Models
	require_once($projectDir.'Models/findEventsModel.php');
?>
