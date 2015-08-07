<?php
	require '../Eventful.php';
	
	ini_set('display_errors',1);
ini_set('display_startup_errors',1);
error_reporting(-1);
	
	function array_to_html($limit, $data) {
		$report = "";
		$row_num = 0;
		if (count($data) > 0) {
			$report .= "<table>";
			$report .= sprintf("<tr><th>%s</th></tr>", join("</th><th>", array_keys($data[0])));
			
			foreach ($data as $row) {
				$row_num++;
				if ($row_num > $limit ) {
					break;
				}
				$report .= "<tr>";
				
				foreach ($row as $column) {
					$report .= "<td>$column</td>";
				}
				$report .= "</tr>";
			}
			$report .= "</table>";
			} else {
			$report = "No data";
		}
		
		return $report;
	}
	
	
	echo  "<title>Eventful PHP API Testing - user locations</title>\n";
	echo  "<p><center>Evenful PHP API testing - user locations</center></p>";
	// Enter your application key here. (See http://api.eventful.com/keys/)
	// Authentication is required for some API methods.
	// Prod 
	$conskey = '22695c02dd81474bf6df';
	$conssec = '68d619ca2ef1d71a4a39';
	$app_key = 'NzRqz6hgC5t3ZR8B';
	$oauth_token        = 'Your OAuth Token goes here';
	$oauth_token_secret = 'Your OAuth Secrete Token goes here';
	/*
		$ev = new Services_Eventful($app_key);
		$ev->set_debug();
		$l = $ev->setup_Oauth($conskey,$conssec,$oauth_token,$oauth_token_secret);
		
		if ( PEAR::isError($l) ) {
		print("Can't setup oauth in: " . $l->getMessage() . "\n");
		}
		
		// Adding 'json' to the end will return the data as a decoded_json array
		$args = array(
		'dummyvar1'   => 'Other data',
		'dummyvar2'   => 'More stuff',
		);
		$locations = $ev->call('users/locales/list', $args, 'json');
		if ( PEAR::isError($locations) ) {
		print("An error occurred: " . $locations->getMessage() . "\n");
		} else {
		print("<br>API Results for locale request are:\n<br>\n");
		print array_to_html(5, $locations[locale]);
		}
	*/
	// Just do a simple lookup
	$ev_noauth = new Services_Eventful($app_key);
	$args = array(
	'location'   => 'San Antonio, ',
	'page_size'  => 50
	);
	$event_info = $ev_noauth->call('events/search', $args, "json");
	//if ( PEAR::isError($event_info) ) {
	//	print("An error occurred: " . $locations->getMessage() . "\n");
	//	} else {
		print("<br>API Results for event lookup:\n<br>\n");
		$data = $event_info;
		print_r ($data);
		
		//$num = $data['page_size'];
		
		$i = 0;
		while ($i < $num){
			//echo $data['events']['event'][$i]['title']." <br>";
			$i++;
		}
		
		//print_r ($json);
	//}
	
?>

