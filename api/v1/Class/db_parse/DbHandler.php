<?php

	use Parse\ParseObject;
	use Parse\ParseUser;
	use Parse\ParseQuery;  
	use Parse\ParseSession;

	class DbHandlerParse {

		function __construct() {
			require_once dirname(__FILE__).'/Config.php';
		}

		/**
		 * Creating new user
		 * @param String $name User full name
		 * @param String $email User login email id
		 * @param String $password User login password
		 */
		public function createUser(CustomParseUser $cpu) {
			$response = array();

			// First check if user already existed in db
			if (!$this->isUserExists($cpu->getUserName(), $cpu->getEmail())) {

				// Generating API key
				$api_key = $this->generateApiKey($cpu->getEmail());

				$user = new ParseUser();
				$user->set("username", $cpu->getUsername());
				$user->set("password", $cpu->getPassword());
				$user->set("email", $cpu->getEmail());
				$user->set("firstName", $cpu->getFirstName());
				$user->set("lastName", $cpu->getLastName());
				$user->set("birthday", $cpu->getBirthday());
				$user->set("apiKey", $api_key);

				try {
				  $user->signUp();
				  // Hooray! Let them use the app now.
				  return 'USER_CREATED_SUCCESSFULLY';
				} catch (Parse\ParseException $ex) {
				  // Show the error message somewhere and let the user try again.
				  echo "Error: " . $ex->getCode() . " " . $ex->getMessage();
				  return 'USER_CREATE_FAILED';
				}
			} else {
				// User with same email already existed in the db
				return 'USER_ALREADY_EXISTED';
			}

			return $response;
		}

		/**
		 * Checking user login
		 * @param String $email User login email id
		 * @param String $password User login password
		 * @return boolean User login status success/fail
		 */
		public function checkLogin($email, $password) {
			$query = ParseUser::query();
			$query->equalTo("email",$email);
			//$query->equalTo("password",$password);
			$results = $query->find();

			if(count($results) > 0){
				$object = $results[0];
			} else{
				return NULL;
			}

			try {
			  $user = ParseUser::logIn($object->get('username'), $password);
			  return $user->getSessionToken();
			  // Do stuff after successful login.
			} catch (Parse\ParseException $error) {
			  // The login failed. Check error to see why.
			  return NULL;
			}
		}

		/**
		 * Checking for duplicate user by email address
		 * @param String $email email to check in db
		 * @return boolean
		 */
		private function isUserExists($username, $email) {
			$query = ParseUser::query();
			$query->equalTo("username",$username);
			$results = $query->find();

			if(count($results) > 0){
				return true;
			} 

			$query = ParseUser::query();
			$query->equalTo("email",$email);
			$results = $query->find();

			if(count($results) > 0){
				return true;
			} 

			return false;
		}
		
		/**
		 * Checking for duplicate user by id
		 * @param String $userId user id to check in db
		 * @return boolean
		 */
		public function isUserIdExists($userId) {
			$query = ParseUser::query();
			$query->equalTo('objectId',$userId);
			$results = $query->find();

			if(count($results) > 0){
				return true;
			} 
			return false;
		}

		/**
		 * Fetching user by email
		 * @param String $email User email
		 */
		public function getUserByEmail($email) {
			$query = ParseUser::query();
			$query->equalTo("email",$email);
			$results = $query->find();

			if(count($results) > 0){
				return $results[0];
			} else{
				return null;
			}
		}
		
		/**
		 * Fetching user by id
		 * @param String $userId User id
		 */
		public function getUserById($userId) {
			$query = ParseUser::query();
			$query->equalTo("objectId",$userId);
			$results = $query->find();

			if(count($results) > 0){
				return $results[0];
			} else{
				return null;
			}
		}

		/**
		 * Fetching user api key
		 * @param String $user_id user id primary key in user table
		 */
		public function getApiKeyById($user_id) {
			$query = ParseUser::query();
			$query->equalTo("objectId",$user_id);
			$results = $query->find();

			if(count($results) > 0){
				return $results[0]->get('apiKey');
			} else{
				return NULL;
			}
		}

		/**
		 * Fetching user session token
		 * @param String $user_id user id primary key in user table
		 */
		public function getSessionTokenById($user_id) {
			$query = ParseUser::query();
			$query->equalTo("objectId",$user_id);
			$results = $query->find();

			if(count($results) > 0){
				$userObj = $results[0];
			} else{
				return NULL;
			}

			$query = ParseSession::query();
			$query->equalTo("user", $userObj);
			$results = $query->find();

			if(count($results) > 0){
				return $results[0]->getSessionToken();
			} else{
				return NULL;
			}
		}

		/**
		 * Fetching user id by api key
		 * @param String $api_key user api key
		 */
		public function getUserId($api_key) {
			$query = ParseUser::query();
			$query->equalTo("apiKey",$api_key);
			$results = $query->find();

			if(count($results) > 0){
				return $results[0]->getObjectId();
			} else{
				return NULL;
			}
		}

		/**
		 * Validating user api key
		 * If the api key is there in db, it is a valid key
		 * @param String $api_key user api key
		 * @return boolean
		 */
		public function isValidApiKey($api_key) {
			$query = ParseUser::query();
			$query->equalTo("apiKey",$api_key);
			$results = $query->find();

			if(count($results) > 0){
				return true;
			} else{
				return false;
			}
		}

		/**
		 * Validating user session token
		 * If the session token matches the api key user, it is a valid token
		 * @param String $session_token user session token
		 * @param String $api_key user api key
		 * @return boolean
		 */
		public function isValidSessionToken($session_token, $api_key) {
			
			// Using already validated $api_key, obtain corresponding user object
			$query = ParseUser::query();
			$query->equalTo("apiKey",$api_key);
			$results = $query->find();

			if(count($results) > 0){
				$userObj = $results[0];
			} else{
				return FALSE;
			}

			try{
				// Become user that has this session token
				// Only was to query back the user that they are
				// If no user is found with this token, parse error
				$thisUser = ParseUser::become($session_token);

				$query = ParseSession::query();
				$query->equalTo("user", $userObj);
				$results = $query->find();

				if(count($results) > 0){
					return TRUE;
				} else{
					return FALSE;
				}
			} catch (Parse\ParseException $error){
				return FALSE;
			}
		}

		/**
		 * Retrive a list of parse users
		 */ 
		public function getUserList(){
			$query = ParseUser::query();
			$query->exists("username");
			$results = $query->find();

			return $results;
		}

		/**
		 * Generating random Unique MD5 String for user Api key
		 */
		private function generateApiKey($emailSalt) {
			return md5(uniqid(rand(), true).$emailSalt);
		}
	}
?>