<?php

	/**
	 * Class to handle all db operations
	 * This class will have CRUD methods for database tables
	 */
	class DbHandlerMySql {

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
	    public function createUser($userId, $username, $email, $createdAt) {
	        $response = array();
	 
	        // First check if user already existed in db
	        if (!$this->isUserExists($userId)) {
	        	echo "Creating User!";

	        	$date = $createdAt->format("Y-m-d H:i:s");

	            // insert query
	            $stmt = $this->conn->prepare("INSERT INTO users(`user_id`, `username`, `email`, `status`, `created_at`) values(?, ?, ?, 1, ?)");
	            $stmt->bind_param("ssss", $userId, $username, $email, $date);
	 
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
	     * Checking for existing user by user id
	     * @param String $userId userId to check in db
	     * @return boolean
	     */
	    public function isUserExists($userId) {
	        $stmt = $this->conn->prepare("SELECT id from users WHERE user_id = ?");
	        $stmt->bind_param("s", $userId);
	        $stmt->execute();
	        $stmt->store_result();
	        $num_rows = $stmt->num_rows;
	        $stmt->close();

	        return $num_rows > 0;
	    }
		
		public function setConn($conn){
			$this->conn = $conn;
		}
		
		public function getConn(){
			return $this->conn;
		}

	}

?>