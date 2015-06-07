<?php
	//ini_set('display_errors',1);
	//ini_set('display_startup_errors',1);
	//error_reporting(-1);
        
	function stubhubAPI_findEvents($queryString, $optionsArray){
		// Keywords from Query String
		$q = $queryString;
		$filterCity = $optionsArray['city'];
		$filterState = $optionsArray['state'];
		$filterDate = $optionsArray['date'];

		$searched_lower = strtolower($q);
		$searched = strtoupper($searched_lower);
		$searchedURL = urlencode($searched); 
	
		$endpoint_stubhub = "http://publicfeed.stubhub.com/listingCatalog/select/";
	
		if(!empty($_POST['ancestorDescriptions'])){
			$ancestorDescriptions = $_POST['ancestorDescriptions'];
		}elseif(empty($_POST['ancestorDescriptions'])){
			$ancestorDescriptions = '';
		}
	
		if(!empty($_POST['sort_what'])){
			$sort_what = $_POST['sort_what'];
		}elseif(empty($_POST['sort_what'])){
			$sort_what = 'event_date_time_local';
		}
	
		if(!empty($_POST['sort_how'])){
			$sort_how = $_POST['sort_how'];
		}elseif(empty($_POST['sort_how'])){
			$sort_how = 'asc';
		}
	
		// StubHub API Query - JSON Response
		$url = "$endpoint_stubhub?q=%252BstubhubDocumentType%253Aevent%250D%250A%252B"
				. "%2Bleaf%253A%2Btrue%250D%250A%252B"
				. "%2Bdescription%253A%2B%22$searchedURL%22%250D%250A%252B"
				. "%3B$sort_what%20$sort_how"
				. "&version=2.2"
				. "&start=0"
				. "&indent=on"
				. "&wt=json"
				. "&fl=description+event_date+event_date_local+event_time_local+geography_parent+venue_name+city+state+genreUrlPath+urlpath+leaf+channel";
	
	
		// Send Request
		$ch = curl_init();
		curl_setopt($ch, CURLOPT_URL, $url);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
		curl_setopt($ch, CURLOPT_REFERER, "http://www.45.55.142.106.com/");
		$body = curl_exec($ch);
	
		curl_close($ch);
	
		// Process JSON string - Convert JSON to PHP Array
		$json = json_decode($body);
		//var_dump($json);
	
		// Number of Returned Results
		$num = $json->response->numFound;

		function contains($str, $content, $ignorecase=true){
			if ($ignorecase){
				$str = strtolower($str);
				$content = strtolower($content);
			}  
			return strpos($content,$str) ? true : false;
		}
		
		$results_events = NULL;
							
		if ($num > 0){
			
			// Today's timestamp date
			$today = strtotime("now");
			
			if(!empty($filterDate)){
				$today = strtotime($filterDate);
			}
			
			// Results Loop 
			$i = 0;
			while ($i<$num) {
				// Filter out results with event title "mirror" - StubHub API anomaly
				if(strstr($json->response->docs[$i]->description,"mirror") == false){
				// Filter out results with event title "coming soon" - StubHub API anomaly
				if(strstr($json->response->docs[$i]->description,"coming soon") == false){
				// Filter out results with event title "posted here" - StubHub API anomaly
				if(strstr($json->response->docs[$i]->description,"posted here") == false){
				// Filter out results with event date earlier than today or date filter
				if (!empty($json->response->docs[$i]->event_date) && strtotime($json->response->docs[$i]->event_date) >= $today){
				// Filter out results not in city
				if (empty($filterCity) || strcasecmp($filterCity,$json->response->docs[$i]->city) == 0){
				// Filter out results not in state
				if (empty($filterState) || strcasecmp($filterState,$json->response->docs[$i]->state) == 0){
					// Result format with JSON variables
					$results_events[$i]['desc'] = $json->response->docs[$i]->description;
					$results_events[$i]['date'] = $json->response->docs[$i]->event_date_local;
					$results_events[$i]['venue'] = $json->response->docs[$i]->venue_name;
					$results_events[$i]['state'] = $json->response->docs[$i]->city;
				}
				}
				}
				}
				}
				}
				// Loop continuance - finite
				$i++;
			}
		}
		
		return $results_events;
	}
?>
