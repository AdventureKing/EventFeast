<?php	
	/**
	 * Creating a friendship between the requesting user and requested user
	 * method POST
	 * url /friendships/create/:id
	 * Will return 404 if the call doesn't belongs to user
	 */
	$app->post('/friendships/create/:id', 'authenticate', function($friendUserId) {
		global $user_id;
		$response = array();
		$db = new FriendShips();

		// fetch call
		$result = $db->createFriendShip($user_id, $friendUserId);

		if ($result != NULL) {
			$response["result"] = "success";
			$response["message"] = "User ID: ".$result["follower_user_id"]." is now following Friend ID: ".$result["followed_user_id"];
			$response["createdAt"] = $result["created"];
			echoRespnse(200, $response);
		} else {
			$response["result"] = "error";
			$response["message"] = "Error: The requesting user was unable to follow friend.";
			echoRespnse(404, $response);
		}
	});
	
	$app->delete('/friendships/destroy/:id', 'authenticate', function($friendUserId) {
		global $user_id;
		$response = array();
		$db = new FriendShips();

		// fetch call
		$result = $db->destroyFriendShip($user_id, $friendUserId);

		if ($result != NULL) {
			$response["result"] = "success";
			$response["message"] = "User ID: ".$result["follower_user_id"]." is no longer following Friend ID: ".$result["followed_user_id"];
			echoRespnse(200, $response);
		} else {
			$response["result"] = "error";
			$response["message"] = "Error: The requesting user was unable to unfollow friend.";
			echoRespnse(404, $response);
		}
	});
?>