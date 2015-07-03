<?php 
	require_once('../../classes/gEvent.php');

	$q = $_GET['q'];
	$optionsArray['city'] = $_GET['city'];

	ticketmasterAPI_findEvents($q, $optionsArray);
	
	function ticketmasterAPI_findEvents($queryString, $optionsArray){
		$q = $queryString;
		$filterCity = $optionsArray['city'];
		$filterState = $optionsArray['state'];
		$filterDate = $optionsArray['date'];
	
		$data = get_data("http://www.ticketmaster.com/json/search/event?q=$queryString");
		$json = json_decode($data);
		$num = $json->response->numFound;
		
		$gEvents = null;
		$i = 0;
		while ($i<$num) {
            
            $gEvent = new gEvent;
            $gEvent->setExternal_id($json->response->docs[$i]->Id);
            $gEvent->setDatasource("ticketmaster");
            $gEvent->setTitle($json->response->docs[$i]->ShortEventName);
            $gEvent->setDescription($json->response->docs[$i]->EventName);

            $gEvent->setVenue_external_id($json->response->docs[$i]->VenueId);
            $gEvent->setVenue_name($json->response->docs[$i]->VenueName);
            $gEvent->setVenue_address($json->response->docs[$i]->VenueAddress);

            $gEvent->setCountry_name($json->response->docs[$i]->VenueCountry);
            $gEvent->setState_name($json->response->docs[$i]->VenueState);
            $gEvent->setCity_name($json->response->docs[$i]->VenueCity);
            $gEvent->setPostal_code($json->response->docs[$i]->VenuePostalCode);

            $gEvent->setLatitude($json->response->docs[$i]->Latitude);
            $gEvent->setLongitude($json->response->docs[$i]->Longitude);
            
            $gEvent->setStart_time(str_replace("T", " ", trim($json->response->docs[$i]->PostProcessedData->LocalEventDate, 0, -6)));           
            
            $gEvent->setEvent_external_url("http://ticketmaster.com".$json->response->docs[$i]->AttractionSEOLink[0]);

            $gEventImages = null;

            foreach ($json->response->docs[$i]->AttractionImage as $image) {
            	$gImage = new gEventImage;
            	$gImage->setImage_external_url("http://s1.ticketm.net/tm/en-us".$image);
            	$gEventImages += $gImage;
            }

            if(isset($json->response->docs[$i]->VenueImage)){
            	$gImage = new gEventImage;
            	$gImage->setImage_external_url("http://s1.ticketm.net/tm/en-us".$json->response->docs[$i]->VenueImage)
            	$gEventImages += $gImage;
            }

            $gEvent->setImages($gEventImages);
            $gEvents += $gEvent;
			
			/*
			if($eventDesc != null && $eventCity == $filterCity){
				$results_events[$i]['desc'] = $eventDesc;
				$results_events[$i]['date'] = $eventDate;
				$results_events[$i]['time'] = $eventStartTime;
				$results_events[$i]['venue'] = $eventVenue;
				$results_events[$i]['city'] = $eventCity;
				$results_events[$i]['state'] = $eventState;
				$results_events[$i]['source'] = $eventSource;
				$results_events[$i]['urlpath'] = $eventURL;
			}
			*/
			
			$i++;
		}
		var_dump(json_encode($gEvents));
		return $gEvents;
	}
	
	function get_data($url)
	{
		$ch1 = curl_init();
		$timeout = 5;
		curl_setopt($ch1,CURLOPT_URL,$url);
		curl_setopt($ch1, CURLOPT_HEADER, 0);
		curl_setopt($ch1,CURLOPT_VERBOSE,1);
		curl_setopt($ch1, CURLOPT_USERAGENT, 'Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 1.0.3705; .NET CLR 1.1.4322; Media Center PC 4.0)');
		curl_setopt ($ch1, CURLOPT_REFERER,'http://www.google.com');  //just a fake referer
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

?>