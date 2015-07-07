<?php
	ini_set('display_errors',1);
	ini_set('display_startup_errors',1);
	error_reporting(-1);
	
	require_once('classes/gEventUtil.php');
	require_once("classes/gEvent.php");
	require_once("classes/curlUtil.php");
	require_once('externalAPIs/stubhub/functions.php');
	require_once('externalAPIs/ticketmaster/functions.php');
        
	$app->get('/findEvents/:eventDesc', function ($eventDesc) use ($app){

		$filters['city'] = $app->request()->params('city');
		$filters['state'] = $app->request()->params('state');
		$filters['date'] = $app->request()->params('date');
		$filters['desc'] = $app->request()->params('desc');
		$filters['sources'] = $app->request()->params('sources');

		if(!empty($filters['sources'])){
			$sourceArr = explode(",",$filters['sources']);
			foreach($sourceArr as $source){
				$stubhub_results = array();
				$ticketmaster_results = array();
				
				if(strcasecmp($source, "stubhub") == 0){ 
					$stubhub_results = stubhubAPI_findEvents($eventDesc, $filters);			
				}
				if(strcasecmp($source, "ticketMaster") == 0){
					$ticketmaster_results = ticketmasterAPI_findEvents($eventDesc, $filters);
				}

				$results = array_merge($stubhub_results, $ticketmaster_results);
			}
		} else { 
			$stubhub_results = stubhubAPI_findEvents($eventDesc, $filters);
			$ticketmaster_results = ticketmasterAPI_findEvents($eventDesc, $filters);
			$results = array_merge($stubhub_results, $ticketmaster_results);			
			usort($results, 'sortgEventsByDate');
		}
		
		$json_response = json_encode($results, JSON_FORCE_OBJECT | JSON_UNESCAPED_SLASHES);
		
		if(!empty($results)){
			//events found
			echo $json_response;
		} else{
			//no results found
			echo json_encode("0 events found");
		}
	});	

?>
