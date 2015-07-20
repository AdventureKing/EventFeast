<?php

	/**
	 * User Registration
	 * url - /user/register
	 * method - POST
	 * params - name, email, password
	 */
	$app->post('/user/register', function() use ($app) {
		// check for required params
		verifyRequiredParams(array('firstName', 'lastName', 'username', 'email', 'password', 'birthday'));

		$response = array();

		$newUser = new CustomParseUser();

		// reading post params
		$newUser->setFirstName($app->request->post('firstName'));
		$newUser->setLastName($app->request->post('lastName'));
		$newUser->setUsername($app->request->post('username'));
		$newUser->setEmail($app->request->post('email'));
		$newUser->setPassword($app->request->post('password'));
		$newUser->setBirthday($app->request->post('birthday'));

		// validating email address
		validateEmail($newUser->getEmail());

		// validating date
		validateDate($newUser->getBirthday());
		$newUser->setBirthday(DateTime::createFromFormat('m/d/Y', $newUser->getBirthday()));

		$db = new DbHandlerParse();
		$res = $db->createUser($newUser);

		if ($res == 'USER_CREATED_SUCCESSFULLY') {
			$response["result"] = 'success';
			$response["message"] = "You are successfully registered";
			echoRespnse(201, $response);
		} else if ($res == 'USER_CREATE_FAILED') {
			$response["result"] = 'error';
			$response["message"] = "Oops! An error occurred while registereing";
			echoRespnse(200, $response);
		} else if ($res == 'USER_ALREADY_EXISTED') {
			$response["result"] = 'error';
			$response["message"] = "Sorry, this username or email is already in use";
			echoRespnse(200, $response);
		}
	});
	
	/**
	 * User Login
	 * url - /login
	 * method - POST
	 * params - email, password
	 */
	$app->post('/user/login', function() use ($app) {
		// check for required params
		verifyRequiredParams(array('email', 'password'));

		// reading post params
		$email = $app->request()->post('email');
		$password = $app->request()->post('password');
		$response = array();

		$db = new DbHandlerParse();
		$sessionToken = $db->checkLogin($email, $password);

		// check for correct email and password
		if ($sessionToken) {
			// get the user by email
			$user = $db->getUserByEmail($email);

			if ($user != NULL) {
				// check if matching user id record in mysql, else create one
				$db_mysql = new DbHandlerMySql();
				$db_mysql->createUser($user->getObjectId(), $user->get('username'), $user->get('email'), $user->getCreatedAt());

				$response["result"] = 'success';
				$response['name'] = $user->get('firstName')." ".$user->get('lastName');
				$response['email'] = $user->get('email');
				$response['apiKey'] = $user->get('apiKey');
				$response['sessionToken'] = $sessionToken;
				$response['createdAt'] = $user->getCreatedAt()->format('Y-m-d H:i:s');
			} else {
				// unknown error occurred
				$response['result'] = 'error';
				$response['message'] = "An error occurred. Please try again";
			}
		} else {
			// user credentials are wrong
			$response['result'] = 'error';
			$response['message'] = 'Login failed. Incorrect credentials';
		}

		echoRespnse(200, $response);
	});

	/**
	 * User Followers
	 * url - /login
	 * method - GET
	 * params - userId
	 */
	$app->post('/user/:userId/followers', function() use ($app) {
		$response = array();

		$db = new DbHandlerMySql();
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