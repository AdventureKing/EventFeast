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

?>