<?php

	class DbHandlerNeo{
		private $neoClient;

		function __construct() {
			require_once dirname(__FILE__).'/Config.php';
			$this->neoClient = $neoClient;
		}

		public function createUser($userId, $email, $username){
			$userExists = $this->isUserExists($email, $username);

			if(!$userExists){
				$query = 'CREATE (user:User {userId: {userId}, email: {email}, username: {username} }) RETURN user';
				$parameters = array('userId' => $userId, 'email' => $email, 'username' => $username);
				$result = $this->neoClient->sendCypherQuery($query, $parameters);
				return 'USER_CREATED_SUCCESSFULLY';
			} else if($userExists){
				return 'USER_ALREADY_EXISTED';
			} else{
				return 'USER_CREATE_FAILED';
			}
		}

		public function isUserExists($email, $username){
			$query = 'MATCH (n) WHERE n.email = {email} OR n.username = {username} RETURN n';
			$parameters = array('email' => $email, 'username' => $username);
			$result = $this->neoClient->sendCypherQuery($query, $parameters);
			return (count($result->getRows()) > 0);
		}

		public function isUserExistsByUserId($userId){
			$query = 'MATCH (n) WHERE n.userId = {userId} RETURN n';
			$parameters = array('userId' => $userId);
			$result = $this->neoClient->sendCypherQuery($query, $parameters);
			return (count($result->getRows()) > 0);
		}

		public function createRelationship($userId1, $relType, $userId2){
			switch($relType){
				case 'FOLLOWS':
					$query = 'MATCH (user1:User {userId:{userId1}}), (user2:User {userId:{userId2}}) CREATE UNIQUE (user1)-[:FOLLOWS]->(user2)';
					$params = ['userId1' => $userId1, 'userId2' => $userId2];
					break;
				default : break;
			}
			
			if($query && $params){
				$this->neoClient->sendCypherQuery($query, $params);
				return true;
			} else{
				return false;
			}
		}

		public function destroyRelationship($userId1, $relType, $userId2){
			switch($relType){
				case 'FOLLOWS':
					$query = 'MATCH (user1:User {userId:{userId1}}), (user2:User {userId:{userId2}}) MATCH (user1)-[follows:FOLLOWS]->(user2) DELETE follows';
					$params = ['userId1' => $userId1, 'userId2' => $userId2];
					break;
				default : break;
			}
			
			if($query && $params){
				$this->neoClient->sendCypherQuery($query, $params);
				return true;
			} else{
				return false;
			}
		}

	}

?>