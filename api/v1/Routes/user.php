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
			echoRespnse(200, $response);
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

	$app->post('/user/neo/create/user', function() use ($app) {
		// check for required params
		verifyRequiredParams(array('userId', 'email', 'username'));

		// reading post params
		$userId = $app->request()->post('userId');
		$email = $app->request()->post('email');
		$username = $app->request()->post('username');

		$db = new DbHandlerNeo();
		$result = $db->createUser($userId, $email, $username);

		switch($result){
			case 'USER_CREATED_SUCCESSFULLY':
				$response['result'] = 'success';
				$response['message'] = 'User Created Successfully.';
				break;
			case 'USER_ALREADY_EXISTED':
				$response['result'] = 'error';
				$response['message'] = 'User already exists.';
				break;
			case 'USER_CREATE_FAILED':
				$response['result'] = 'error';
				$response['message'] = 'User creation failed.';
				break;
			default : 
				$response['result'] = 'error';
				$response['message'] = 'Unknown issue.';
				break;
		}

		echoRespnse(200, $response);
	});

	$app->post('/user/neo/create/relationship', function() use ($app) {
		// check for required params
		verifyRequiredParams(array('userId1', 'relType', 'userId2'));

		// reading post params
		$userId1 = $app->request()->post('userId1');
		$relType = $app->request()->post('relType');
		$userId2 = $app->request()->post('userId2');

		$db = new DbHandlerNeo();
		if($db->isUserExistsByUserId($userId1) && $db->isUserExistsByUserId($userId2)){
			$result = $db->createRelationship($userId1, $relType, $userId2);
		} else{
			$result = false;
		}
		
		if($result){
			$response['result'] = 'success';
			$response['message'] = "User ID: $userId1 now $relType User ID: $userId2.";
		} else{
			$response['result'] = 'error';
			$response['message'] = 'Relationship creation failed.';
		}

		echoRespnse(200, $response);
	});

	$app->post('/user/neo/destroy/relationship', function() use ($app) {
		// check for required params
		verifyRequiredParams(array('userId1', 'relType', 'userId2'));

		// reading post params
		$userId1 = $app->request()->post('userId1');
		$relType = $app->request()->post('relType');
		$userId2 = $app->request()->post('userId2');

		$db = new DbHandlerNeo();
		if($db->isUserExistsByUserId($userId1) && $db->isUserExistsByUserId($userId2)){
			$result = $db->destroyRelationship($userId1, $relType, $userId2);
		} else{
			$result = false;
		}
		
		if($result){
			$response['result'] = 'success';
			$response['message'] = "User ID: $userId1 no longer $relType User ID: $userId2.";
		} else{
			$response['result'] = 'error';
			$response['message'] = 'Relationship removal failed.';
		}

		echoRespnse(200, $response);
	});



?>