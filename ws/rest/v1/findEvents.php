<?php
	//ini_set('display_errors',1);
	//ini_set('display_startup_errors',1);
	//error_reporting(-1);
	
	require_once('externalAPIs/stubhub/functions.php');
        
	$app->get('/findEvents/:eventDesc', function ($eventDesc) use ($app){

		$filters['city'] = $app->request()->params('city');
		$filters['state'] = $app->request()->params('state');
		$filters['date'] = $app->request()->params('date');
		$filters['sources'] = $app->request()->params('sources');
		
		if(!empty($filters['sources'])){
			$sourceArr = explode(",",$filters['sources']);
			foreach($sourceArr as $source){
				if(strcasecmp($source, "stubhub") == 0){ 
					$results = stubhubAPI_findEvents($eventDesc, $filters); 			
				}
				if(strcasecmp($source, "ticketMaster") == 0){
					//$results +=;
				}
				if(strcasecmp($source, "yelp") == 0){
					//$results +=;
				}
			}
		} else { 
			$results = stubhubAPI_findEvents($eventDesc, $filters);
		}
		
		$json_response = json_encode($results);
		
		if(!empty($results)){
			//events found
			echo $json_response;
		} else{
			//no results found
			echo json_encode("0 events found");
		}
	});	

?>
