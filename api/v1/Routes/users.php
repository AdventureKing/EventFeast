<?php
	/**
	 * Users List
	 * url - /list
	 * method - GET
	 */
	$app->get('/users/list', 'authenticate', function() use ($app) {
		$response = array();

		$db = new DbHandlerParse();
		$results = $db->getUserList();
		$records = array();

		//echo "Successfully retrieved " . count($results) . " scores.<br><br>";
		// Do something with the returned ParseObject values
		for ($i = 0; $i < count($results); $i++) {
	  		$object = $results[$i];

	  		$record = array();
	  		$records[$i]['userId'] = $object->getObjectId();
	  		$records[$i]['firstName'] = $object->get('firstName');
	  		$records[$i]['lastName'] = $object->get('lastName');
	  		$records[$i]['username'] = $object->get('username');
	  		$records[$i]['email'] = $object->get('email');

	  		//echo $object->getObjectId() . ' - ' . $object->get('username') . '<br>';
		}

		// check for records returned
		if ($records) {
			$response["result"] = "success";
			$response['message'] = count($records)." users found.";
			$response['items'] = $records;
			
		} else {
			// no records found
			$response['result'] = 'success';
			$response['message'] = 'No Users Found';
		}

		echoRespnse(200, $response);
	});
?>