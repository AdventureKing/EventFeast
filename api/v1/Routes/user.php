<?php

	/**
	 * User Registration
	 * url - /user/register
	 * method - POST
	 * params - name, email, password
	 */
	$app->post('/user/register', function() use ($app) {
		// check for required params
		verifyRequiredParams(array('name', 'email', 'password'));

		$response = array();

		// reading post params
		$name = $app->request->post('name');
		$email = $app->request->post('email');
		$password = $app->request->post('password');

		// validating email address
		validateEmail($email);

		$db = new DbHandler();
		$res = $db->createUser($name, $email, $password);

		if ($res == USER_CREATED_SUCCESSFULLY) {
			$response["error"] = false;
			$response["message"] = "You are successfully registered";
			echoRespnse(201, $response);
		} else if ($res == USER_CREATE_FAILED) {
			$response["error"] = true;
			$response["message"] = "Oops! An error occurred while registereing";
			echoRespnse(200, $response);
		} else if ($res == USER_ALREADY_EXISTED) {
			$response["error"] = true;
			$response["message"] = "Sorry, this email already existed";
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

		$db = new DbHandler();
		// check for correct email and password
		if ($db->checkLogin($email, $password)) {
			// get the user by email
			$user = $db->getUserByEmail($email);

			if ($user != NULL) {
				$response["error"] = false;
				$response['name'] = $user['name'];
				$response['email'] = $user['email'];
				$response['apiKey'] = $user['api_key'];
				$response['createdAt'] = $user['created_at'];
			} else {
				// unknown error occurred
				$response['error'] = true;
				$response['message'] = "An error occurred. Please try again";
			}
		} else {
			// user credentials are wrong
			$response['error'] = true;
			$response['message'] = 'Login failed. Incorrect credentials';
		}

		echoRespnse(200, $response);
	});
			
?>