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
				return NULL;
			}
			
			// Verify that not already following
			$stmt = $this->conn->prepare("SELECT f.follower_user_id, f.followed_user_id, f.status, f.created from follow f WHERE f.follower_user_id = ? AND f.followed_user_id = ?");
			$stmt->bind_param("ss", $userId, $friendUserId);
			if ($stmt->execute()) {
				$ret = $stmt->get_result()->fetch_assoc();
				$stmt->close();

				if(!empty($ret)){
					//echo "user is already following friend!";
					return NULL;
				}
			}
			
			$db_mark = new DbHandlerMark();
			$db_mark->run_sql("INSERT INTO follow(follower_user_id,followed_user_id,status) VALUES('$userId','$friendUserId','1')");

			$stmt = $this->conn->prepare("INSERT INTO follow(follower_user_id,followed_user_id,status) VALUES(?,?,'1')");
			$stmt->bind_param("ss", $userId, $friendUserId);
			$result = $stmt->execute();
			$stmt->close();

			if ($result) {
				// follow row created
				$stmt = $this->conn->prepare("SELECT f.follower_user_id, f.followed_user_id, f.status, f.created from follow f WHERE f.follower_user_id = ? AND f.followed_user_id = ?");
				$stmt->bind_param("ss", $userId, $friendUserId);
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
			$stmt->bind_param("ss", $userId, $friendUserId);
			if ($stmt->execute()) {
				$ret = $stmt->get_result()->fetch_assoc();
				$stmt->close();

				if(empty($ret)){
					//echo "user is not already following friend!";
					return NULL;
				}
			}

			$db_mark = new DbHandlerMark();
			$db_mark->run_sql("DELETE from follow WHERE follower_user_id = '$userId' AND followed_user_id = '$friendUserId'");
			
			$stmt = $this->conn->prepare("DELETE from follow WHERE follower_user_id = ? AND followed_user_id = ?");
			$stmt->bind_param("ss", $userId, $friendUserId);
			$result = $stmt->execute();
			$stmt->close();

			if ($result) {
				// follow row removed
				$stmt = $this->conn->prepare("SELECT f.follower_user_id, f.followed_user_id, f.status, f.created from follow f WHERE f.follower_user_id = ? AND f.followed_user_id = ?");
				$stmt->bind_param("ss", $userId, $friendUserId);
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
	}
?>