<?php
	class FriendShips extends DbHandlerMySql{
		private $conn;
	
		function __construct() {
			parent::__construct();
			$this->conn = parent::getConn();
		}
		
		/**
		 * Creating new friendship between requeting user and requested user
		 * @param String $userId user id to whom call belongs to
		 * @param String $friendUserId user id of the requested user
		 */
		public function createFriendShip($userId, $friendUserId) {        
			// Verify that requested friend exists
			$db_parse = new DbHandlerParse();
			$isFriendExist = $db_parse->isUserIdExists($friendUserId);
			
			if(!$isFriendExist || empty($userId) || $userId == $friendUserId){
				echo $isFriendExist;
				return NULL;
			}
			
			// Verify that not already following
			$stmt = $this->conn->prepare("SELECT f.follower_user_id, f.followed_user_id, f.status, f.created from follow f WHERE f.follower_user_id = ? AND f.followed_user_id = ?");
			$stmt->bind_param("ii", $userId, $friendUserId);
			if ($stmt->execute()) {
				$ret = $stmt->get_result()->fetch_assoc();
				$stmt->close();

				if(!empty($ret)){
					//echo "user is already following friend!";
					return NULL;
				}
			}
			
			$stmt = $this->conn->prepare("INSERT INTO follow(follower_user_id,followed_user_id,status) VALUES(?,?,'1')");
			$stmt->bind_param("ss", $userId, $friendUserId);
			$result = $stmt->execute();
			$stmt->close();

			if ($result) {
				// follow row created
				$stmt = $this->conn->prepare("SELECT f.follower_user_id, f.followed_user_id, f.status, f.created from follow f WHERE f.follower_user_id = ? AND f.followed_user_id = ?");
				$stmt->bind_param("ii", $userId, $friendUserId);
				if ($stmt->execute()) {
					$ret = $stmt->get_result()->fetch_assoc();
					$stmt->close();
					if(!empty($ret)){
						//echo "user is now following friend!";
						return $ret;
					}
					
				} else {
					return NULL;
				}
			} else {
				// follow request failed
				//echo "uh oh, why can't you follow friend?";
				return NULL;
			}
		}
		
		/**
		 * Destroy friendship between requesting user and requested user
		 * @param String $userId user id to whom call belongs to
		 * @param String $friendUserId user id of the requested user
		 */
		public function destroyFriendShip($userId, $friendUserId) {        
			// Verify that requested friend exists
			$db_parse = new DbHandlerParse();
			$isFriendExist = $db_parse->isUserIdExists($friendUserId);
			
			if(!$isFriendExist || empty($userId) || $userId == $friendUserId){
				echo $isFriendExist;
				return NULL;
			}
			
			// Verify that already following
			$stmt = $this->conn->prepare("SELECT f.follower_user_id, f.followed_user_id, f.status, f.created from follow f WHERE f.follower_user_id = ? AND f.followed_user_id = ?");
			$stmt->bind_param("ii", $userId, $friendUserId);
			if ($stmt->execute()) {
				$ret = $stmt->get_result()->fetch_assoc();
				$stmt->close();

				if(empty($ret)){
					//echo "user is not already following friend!";
					return NULL;
				}
			}
			
			$stmt = $this->conn->prepare("DELETE from follow WHERE follower_user_id = ? AND followed_user_id = ?");
			$stmt->bind_param("ss", $userId, $friendUserId);
			$result = $stmt->execute();
			$stmt->close();

			if ($result) {
				// follow row removed
				$stmt = $this->conn->prepare("SELECT f.follower_user_id, f.followed_user_id, f.status, f.created from follow f WHERE f.follower_user_id = ? AND f.followed_user_id = ?");
				$stmt->bind_param("ii", $userId, $friendUserId);
				if ($stmt->execute()) {
					$ret = $stmt->get_result()->fetch_assoc();
					$stmt->close();
					if(empty($ret)){
						//echo "user is no longer following friend!";
						return TRUE;
					}
					
				} else {
					return NULL;
				}
			} else {
				// unfollow request failed
				//echo "uh oh, why can't you unfollow friend?";
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