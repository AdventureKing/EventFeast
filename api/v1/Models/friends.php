<?php
	class Friends extends DbHandlerMySql{
		private $conn;
	
		function __construct() {
			parent::__construct();
			$this->conn = parent::getConn();
		}
		
		public function getFriends($userId) {  
			$db_parse = new DbHandlerParse();
			$usersArray = array();

			$db_mark = new DbHandlerMark();
			$db_mark->run_sql("SELECT * FROM follow WHERE follower_user_id = '$userId'");
			
			$stmt = $this->conn->prepare("SELECT * FROM follow WHERE follower_user_id = ?");
			$stmt->bind_param("s", $userId);
			if ($stmt->execute()) {
				/* Store the result (to get properties) */
			   $stmt->store_result();

			   /* Get the number of rows */
			   $num_of_rows = $stmt->num_rows;

			   /* Bind the result to variables */
			   $stmt->bind_result($id, $followerUserId, $followedUserId, $status, $createdAt);

			   while ($stmt->fetch()) {
					//echo 'ID: '.$id.'<br>';
					//echo 'Follower: '.$followerUserId.'<br>';
					//echo 'Followed: '.$followedUserId.'<br>';
					//echo 'status: '.$status.'<br>';
					//echo 'Created: '.$createdAt.'<br><br>';
					
					//printf("Follower ID: %s  Created: %s", $followerUserId, $createdAt);  
					array_push($usersArray, $db_parse->getUserById($followedUserId));
			   }

			   /* free results */
			   $stmt->free_result();
			}

			$stmt->close();
			
			return $usersArray;
		}
		
	}
?>