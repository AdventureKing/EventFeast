<?php
	class DbHandlerMark {

		private $userId = 2236;
		private $sessionId = 5263;
		private $sessionSalt = "a265527209f71bfd825e6695667f79d1ba3d86a8c60f0189da4d7d5f34f03b42";

		public function hello(){
			// Test hello web service
			$action = 'hello';
			$params = array();
			if($this->isAuthenticated()){
				$this->call_marks_web_service($action, $params);
			} else{
				$this->login();
				$this->call_marks_web_service($action, $params);
			}
		}

		public function run_sql($query){
			// Execute sql queries using web service call
			$action = 'run_sql';
			$params = array(
				'query' => $query,
				'args'  => ''
			);
			if($this->isAuthenticated()){
				$this->call_marks_web_service($action, $params);
			} else{
				$this->login();
				$this->call_marks_web_service($action, $params);
			}	
		}

		private function login(){
			// Authenticate with Marks web service
			$action = 'login';
			$params = array(
				'login' => 'ws_team05',
			    'password' => hash('sha256', 'eMpbKLhhFLFjHYns'),
			    'app_code' => 'L8qtSbEhVX'
			);
			$this->call_marks_web_service($action, $params);
		}

		private function isAuthenticated(){
			if($this->userId == null || $this->sessionId == null || $this->sessionSalt == null){
				//echo "Not Authenticated with Robinsons API.";
				return false;
			}
			return true;
		}

		/*
			Wrapper function to call marks web services
		 */
		private function call_marks_web_service($action, $params){
			// Create url that the web service uses
			$url = 'https://devcloud.fulgentcorp.com/bifrost/ws.php';
			$url .= '?json=';

			// Initialize posible variables
			$login = null;
			$password = null;
			$appCode = null;
			$query = null;
			$args = null;
			$checksum = null;
			
			// Post, Result, and Message variables
			$postdata = null;
			$result = null;
			$message = null;

			// Loop through each parameter and set possible function variables
			foreach ($params as $key => $value) {
	    		switch($key){
	    			case 'login':
	    				$login 		= $value;
	    				break;
	    			case 'password':
	    				$password 	= $value;
	    				break;
	    			case 'app_code':
	    				$appCode 	= $value;
	    				break;
	    			case 'query':
	    				$query 		= $value;
	    				break;
	    			case 'args':
	    				$args 		= $value;
	    				break;
	    			default:
	    				break;
	    		}
			}

			// Depending on action (aka web service) create different postdata and checksums
			switch($action){
				case 'login':
					$postdata = '[{"action":"'.$action.'"},{"login":"'.$login.'"},{"password":"'.$password.'"},{"app_code":"'.$appCode.'"}]';
					$checksum = ',{"checksum":"'.hash('sha256', $postdata).'"}]';
					break;
				case 'hello':
					$postdata = '[{"action":"'.$action.'"},{"session_id":'.$this->sessionId.'}]';
					$checksum = ',{"checksum":"'.hash('sha256', $this->sessionSalt).'"}]';
					break;
				case 'run_sql':
					$postdata = '[{"action":"'.$action.'"},{"session_id":'.$this->sessionId.'},{"query":"'.$query.'"}]';
					$checksum = ',{"checksum":"'.hash('sha256', $postdata.$this->sessionSalt).'"}]';
					break;
				default:
					break;
			}

			// Append checksum to postdata and encode postdata so that it is safe for url (Ex. converts blank spaces to %20)
			$postdata = substr($postdata, 0, -1).$checksum;
			$postdata = urlencode($postdata);
			$url .= $postdata;

			// Get the results of the url request
			$return_data = $this->get_data($url);
			$return_json = json_decode($return_data, true);

			// Loop thorugh the json results and obtain result variables
			foreach($return_json as $item) {
				if(!empty($item["result"])){
					$result = $item["result"];
				}
				if(!empty($item["message"])){
					$message = $item["message"];
				}
				if(!empty($item["user_id"])){
					$this->userId = $item["user_id"];
				}
				if(!empty($item["session_id"])){
					$this->sessionId  = $item["session_id"];
				}
				if(!empty($item["session_salt"])){
					$this->sessionSalt = $item["session_salt"];
				}
			}
			
			// Print results out to the user
			//echo "<b>".strtoupper($action)." Results</b><br><br>";
			//echo "Result: ".$result."<br>";
			//echo "Message: ".$message."<br>";
			
			if($action == 'login'){
				//echo "User ID: ".$this->userId."<br>";
				//echo "Session ID: ".$this->sessionId."<br>";
				//echo "Session Salt: ".$this->sessionSalt."<br><br>";
			}

			//echo 'POST DATA: '.urldecode($postdata).'<br><br>';
			echo 'RETURN DATA: '.$return_data.'<br>';
			//echo 'URL: '.$url."<hr></hr>";
		}	

		/*
			cURL resquest, takes in url and returns results
		 */
		private function get_data($url){
			$ch1 = curl_init();
			$timeout = 5;
			curl_setopt($ch1,CURLOPT_URL,$url);
			curl_setopt($ch1, CURLOPT_HEADER, 0);
			curl_setopt($ch1,CURLOPT_VERBOSE,1);
			curl_setopt($ch1, CURLOPT_RETURNTRANSFER, 1);
			curl_setopt($ch1,CURLOPT_POST,0);
			curl_setopt($ch1, CURLOPT_FOLLOWLOCATION, 20);
			$data = curl_exec($ch1);

			if(!curl_errno($ch1)){ 
				return $data;
			}else{
				echo 'Curl error: ' . curl_error($ch1); 
			}
			curl_close($ch1);
		}
	}
?>