<?php
	/**
	 * Creating new call in db
	 * method POST
	 * params - name
	 * url - /calls/
	 */
	$app->post('/calls', 'authenticate', function() use ($app) {
		// check for required params
		verifyRequiredParams(array('call'));

		$response = array();
		$call = $app->request->post('call');

		global $user_id;
		$db = new DbHandler();

		// creating new call
		$call_id = $db->createCall($user_id, $call);

		if ($call_id != NULL) {
			$response["error"] = false;
			$response["message"] = "Call created successfully";
			$response["call_id"] = $call_id;
		} else {
			$response["error"] = true;
			$response["message"] = "Failed to create call. Please try again";
		}
		echoRespnse(201, $response);
	});
	
	/**
	 * Listing all calls of particual user
	 * method GET
	 * url /calls          
	 */
	$app->get('/calls', 'authenticate', function() {
		global $user_id;
		$response = array();
		$db = new DbHandler();

		// fetching all user calls
		$result = $db->getAllUserCalls($user_id);

		$response["error"] = false;
		$response["calls"] = array();

		// looping through result and preparing calls array
		while ($call = $result->fetch_assoc()) {
			$tmp = array();
			$tmp["id"] = $call["id"];
			$tmp["call"] = $call["call"];
			$tmp["status"] = $call["status"];
			$tmp["createdAt"] = $call["created_at"];
			array_push($response["calls"], $tmp);
		}

		echoRespnse(200, $response);
	});
	
	/**
	 * Listing single call of particual user
	 * method GET
	 * url /calls/:id
	 * Will return 404 if the call doesn't belongs to user
	 */
	$app->get('/calls/:id', 'authenticate', function($call_id) {
		global $user_id;
		$response = array();
		$db = new DbHandler();

		// fetch call
		$result = $db->getCall($call_id, $user_id);

		if ($result != NULL) {
			$response["error"] = false;
			$response["id"] = $result["id"];
			$response["call"] = $result["call"];
			$response["status"] = $result["status"];
			$response["createdAt"] = $result["created_at"];
			echoRespnse(200, $response);
		} else {
			$response["error"] = true;
			$response["message"] = "The requested resource doesn't exists";
			echoRespnse(404, $response);
		}
	});
?>