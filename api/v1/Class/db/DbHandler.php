<?php

	/**
	 * Class to handle all db operations
	 * This class will have CRUD methods for database tables
	 */
	class DbHandler {

		private $conn;

		function __construct() {
			require_once dirname(__FILE__) . '/DbConnect.php';
			// opening db connection
			$db = new DbConnect();
			$this->conn = $db->connect();
		}

		/* ------------- `users` table method ------------------ */

		/**
		 * Creating new user
		 * @param String $name User full name
		 * @param String $email User login email id
		 * @param String $password User login password
		 */
		public function createUser($name, $email, $password) {
			require_once 'PassHash.php';
			$response = array();

			// First check if user already existed in db
			if (!$this->isUserExists($email)) {
				// Generating password hash
				$password_hash = PassHash::hash($password);

				// Generating API key
				$api_key = $this->generateApiKey();

				// insert query
				$stmt = $this->conn->prepare("INSERT INTO users(name, email, password_hash, api_key, status) values(?, ?, ?, ?, 1)");
				$stmt->bind_param("ssss", $name, $email, $password_hash, $api_key);

				$result = $stmt->execute();

				$stmt->close();

				// Check for successful insertion
				if ($result) {
					// User successfully inserted
					return USER_CREATED_SUCCESSFULLY;
				} else {
					// Failed to create user
					return USER_CREATE_FAILED;
				}
			} else {
				// User with same email already existed in the db
				return USER_ALREADY_EXISTED;
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
			// fetching user by email
			$stmt = $this->conn->prepare("SELECT password_hash FROM users WHERE email = ?");

			$stmt->bind_param("s", $email);

			$stmt->execute();

			$stmt->bind_result($password_hash);

			$stmt->store_result();

			if ($stmt->num_rows > 0) {
				// Found user with the email
				// Now verify the password

				$stmt->fetch();

				$stmt->close();

				if (PassHash::check_password($password_hash, $password)) {
					// User password is correct
					return TRUE;
				} else {
					// user password is incorrect
					return FALSE;
				}
			} else {
				$stmt->close();

				// user not existed with the email
				return FALSE;
			}
		}

		/**
		 * Checking for duplicate user by email address
		 * @param String $email email to check in db
		 * @return boolean
		 */
		private function isUserExists($email) {
			$stmt = $this->conn->prepare("SELECT id from users WHERE email = ?");
			$stmt->bind_param("s", $email);
			$stmt->execute();
			$stmt->store_result();
			$num_rows = $stmt->num_rows;
			$stmt->close();
			return $num_rows > 0;
		}

		/**
		 * Fetching user by email
		 * @param String $email User email id
		 */
		public function getUserByEmail($email) {
			$stmt = $this->conn->prepare("SELECT name, email, api_key, status, created_at FROM users WHERE email = ?");
			$stmt->bind_param("s", $email);
			if ($stmt->execute()) {
				$user = $stmt->get_result()->fetch_assoc();
				$stmt->close();
				return $user;
			} else {
				return NULL;
			}
		}

		/**
		 * Fetching user api key
		 * @param String $user_id user id primary key in user table
		 */
		public function getApiKeyById($user_id) {
			$stmt = $this->conn->prepare("SELECT api_key FROM users WHERE id = ?");
			$stmt->bind_param("i", $user_id);
			if ($stmt->execute()) {
				$api_key = $stmt->get_result()->fetch_assoc();
				$stmt->close();
				return $api_key;
			} else {
				return NULL;
			}
		}

		/**
		 * Fetching user id by api key
		 * @param String $api_key user api key
		 */
		public function getUserId($api_key) {
			$stmt = $this->conn->prepare("SELECT id FROM users WHERE api_key = ?");
			$stmt->bind_param("s", $api_key);
			if ($stmt->execute()) {
				$user_id = $stmt->get_result()->fetch_assoc();
				$stmt->close();
				return $user_id;
			} else {
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
			$stmt = $this->conn->prepare("SELECT id from users WHERE api_key = ?");
			$stmt->bind_param("s", $api_key);
			$stmt->execute();
			$stmt->store_result();
			$num_rows = $stmt->num_rows;
			$stmt->close();
			return $num_rows > 0;
		}

		/**
		 * Generating random Unique MD5 String for user Api key
		 */
		private function generateApiKey() {
			return md5(uniqid(rand(), true));
		}

		/* ------------- `calls` table method ------------------ */

		/**
		 * Creating new call
		 * @param String $user_id user id to whom call belongs to
		 * @param String $call call text
		 */
		public function createCall($user_id, $call) {        
			$stmt = $this->conn->prepare("INSERT INTO calls(call) VALUES(?)");
			$stmt->bind_param("s", $call);
			$result = $stmt->execute();
			$stmt->close();

			if ($result) {
				// call row created
				// now assign the call to user
				$new_call_id = $this->conn->insert_id;
				$res = $this->createUserCall($user_id, $new_call_id);
				if ($res) {
					// call created successfully
					return $new_call_id;
				} else {
					// call failed to create
					return NULL;
				}
			} else {
				// call failed to create
				return NULL;
			}
		}

		/**
		 * Fetching single call
		 * @param String $call_id id of the call
		 */
		public function getCall($call_id, $user_id) {
			$stmt = $this->conn->prepare("SELECT c.id, c.call, c.status, c.created_at from calls c, user_calls uc WHERE c.id = ? AND uc.call_id = c.id AND uc.user_id = ?");
			$stmt->bind_param("ii", $call_id, $user_id);
			if ($stmt->execute()) {
				$call = $stmt->get_result()->fetch_assoc();
				$stmt->close();
				return $call;
			} else {
				return NULL;
			}
		}

		/**
		 * Fetching all user calls
		 * @param String $user_id id of the user
		 */
		public function getAllUserCalls($user_id) {
			$stmt = $this->conn->prepare("SELECT c.* FROM calls c, user_calls uc WHERE c.id = uc.call_id AND uc.user_id = ?");
			$stmt->bind_param("i", $user_id);
			$stmt->execute();
			$calls = $stmt->get_result();
			$stmt->close();
			return $calls;
		}

		/**
		 * Updating call
		 * @param String $call_id id of the call
		 * @param String $call call text
		 * @param String $status call status
		 */
		public function updateCall($user_id, $call_id, $call, $status) {
			$stmt = $this->conn->prepare("UPDATE calls c, user_calls uc set c.call = ?, c.status = ? WHERE c.id = ? AND c.id = uc.call_id AND uc.user_id = ?");
			$stmt->bind_param("siii", $call, $status, $call_id, $user_id);
			$stmt->execute();
			$num_affected_rows = $stmt->affected_rows;
			$stmt->close();
			return $num_affected_rows > 0;
		}

		/**
		 * Deleting a call
		 * @param String $call_id id of the call to delete
		 */
		public function deleteCall($user_id, $call_id) {
			$stmt = $this->conn->prepare("DELETE c FROM calls c, user_calls uc WHERE c.id = ? AND uc.call_id = c.id AND uc.user_id = ?");
			$stmt->bind_param("ii", $call_id, $user_id);
			$stmt->execute();
			$num_affected_rows = $stmt->affected_rows;
			$stmt->close();
			return $num_affected_rows > 0;
		}

		/* ------------- `user_calls` table method ------------------ */

		/**
		 * Function to assign a call to user
		 * @param String $user_id id of the user
		 * @param String $call_id id of the call
		 */
		public function createUserCall($user_id, $call_id) {
			$stmt = $this->conn->prepare("INSERT INTO user_calls(user_id, call_id) values(?, ?)");
			$stmt->bind_param("ii", $user_id, $call_id);
			$result = $stmt->execute();
			$stmt->close();
			return $result;
		}

	}

?>