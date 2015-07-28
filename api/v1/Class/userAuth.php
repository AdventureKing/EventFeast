<?php	
	/*******************************************************************
	 * User Auth
	 *******************************************************************/

	/**
	 * Verifying required params posted or not
	 */
	function verifyRequiredParams($required_fields) {
		$error = false;
		$error_fields = "";
		$request_params = array();
		$request_params = $_REQUEST;
		// Handling PUT request params
		if ($_SERVER['REQUEST_METHOD'] == 'PUT') {
			$app = \Slim\Slim::getInstance();
			parse_str($app->request()->getBody(), $request_params);
		}
		foreach ($required_fields as $field) {
			if (!isset($request_params[$field]) || strlen(trim($request_params[$field])) <= 0) {
				$error = true;
				$error_fields .= $field . ', ';
			}
		}

		if ($error) {
			// Required field(s) are missing or empty
			// echo error json and stop the app
			$response = array();
			$app = \Slim\Slim::getInstance();
			$response["result"] = "error";
			$response["message"] = 'Required field(s) ' . substr($error_fields, 0, -2) . ' is missing or empty';
			echoRespnse(400, $response);
			$app->stop();
		}
	}

	/**
	 * Validating email address
	 */
	function validateEmail($email) {
		$app = \Slim\Slim::getInstance();
		if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
			$response["result"] = "error";
			$response["message"] = 'Email address is not valid';
			echoRespnse(400, $response);
			$app->stop();
		}
	}

	/**
	 * Validating date
	 */
	function validateDate($date) {
		$app = \Slim\Slim::getInstance();
		if (!DateTime::createFromFormat('m/d/Y', $date)) {
			$response["result"] = "error";
			$response["message"] = 'Date is not valid format (m/d/Y)';
			echoRespnse(400, $response);
			$app->stop();
		}
	}

	/**
	 * Echoing json response to client
	 * @param String $status_code Http response code
	 * @param Int $response Json response
	 */
	function echoRespnse($status_code, $response) {
		$app = \Slim\Slim::getInstance();
		// Http response code
		$app->status($status_code);

		// setting response content type to json
		$app->contentType('application/json');

		echo json_encode($response, JSON_FORCE_OBJECT | JSON_UNESCAPED_SLASHES);
	}
	
	/**
	 * Adding Middle Layer to authenticate every request
	 * Checking if the request has valid api key in the 'Authorization' header
	 */
	function authenticate(\Slim\Route $route) {
		// Getting request headers
		$headers = apache_request_headers();
		$response = array();
		$app = \Slim\Slim::getInstance();

		// Verifying Authorization Header
		if (isset($headers['Authorization']) && isset($headers['Token'])) {
			$db = new DbHandlerParse();

			// get the api key
			$api_key = $headers['Authorization'];
			// get the session token
			$session_token = $headers['Token'];

			// validating api key
			if (!$db->isValidApiKey($api_key)) {
				// api key is not present in users table
				$response["result"] = "error";
				$response["message"] = "Access Denied. Invalid Api key";
				echoRespnse(401, $response);
				$app->stop();
			} else if(!$db->isValidSessionToken($session_token, $api_key)) {
				// session token does not match api key or is just invalid
				$response["result"] = "error";
				$response["message"] = "Access Denied. Invalid Token";
				echoRespnse(401, $response);
				$app->stop();
			} else {
				global $user_id;
				// get user primary key id
				$userID = $db->getUserId($api_key);
				if (NULL != $userID){
					$user_id = $userID;
					$_SESSION['userId'] = $user_id;
				}
			}
		} else if(!isset($headers['Authorization'])){
			// api key is missing in header
			$response["result"] = "error";
			$response["message"] = "Api key is misssing";
			echoRespnse(400, $response);
			$app->stop();
		} else {
			// token is missing in header
			$response["result"] = "error";
			$response["message"] = "Token is misssing";
			echoRespnse(400, $response);
			$app->stop();
		}
	}
?>
	