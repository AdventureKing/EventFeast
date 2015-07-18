<?php

	/**
	 * Class to handle all db operations
	 * This class will have CRUD methods for database tables
	 */
	class DbHandlerMySql extends DbHandlerParse{

		private $conn;

		function __construct() {
			require_once dirname(__FILE__) . '/DbConnect.php';
			// opening db connection
			$db = new DbConnect();
			$this->conn = $db->connect();
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