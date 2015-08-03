<?php 
	require_once($projectDir.'Class/gEvent.php');
	require_once($projectDir.'Class/curlUtil.php');

	function stubhubAPI_findEvents($queryString, $optionsArray){
		
		// Filter Parameters
		$q = urlencode(strtolower($queryString));
		$userAddress = $optionsArray['address'];
		$filterCity = $optionsArray['city'];
		$filterState = $optionsArray['state'];
		$filterDate = $optionsArray['date'];
		$filterDesc = $optionsArray['desc'];
		$filterRadius = $optionsArray['radius'];

		if(null != $userAddress){
			$location = getLatLongFromAddress($userAddress);
			$userLat = $location['latitude'];
			$userLong = $location['longitude'];
		} else{
			$userLat = null;
			$userLong = null;
		}

		$endpoint_stubhub = "http://publicfeed.stubhub.com/listingCatalog/select/";
		$url = "$endpoint_stubhub?q=$q&wt=json&stubhubDocumentType=event&rows=50&city=$filterCity";
		$data = get_data($url);
		$json = json_decode($data);
		$num = $json->response->numFound;

		$gEvents = array();
		$i = 0;
		
		// Loop through Json Results from CURL resuest
		while ($i < $num) {

            $externalId = empty($json->response->docs[$i]->event_id) ? null : $json->response->docs[$i]->event_id;
			$city = empty($json->response->docs[$i]->city) ? null : $json->response->docs[$i]->city;
			$state = empty($json->response->docs[$i]->state) ? null : $json->response->docs[$i]->state;
			$date = empty($json->response->docs[$i]->event_date) ? null : substr($json->response->docs[$i]->event_date, 0, -10);
			$desc = empty($json->response->docs[$i]->description) ? null : $json->response->docs[$i]->description;
			
			$title = empty($json->response->docs[$i]->title) ? null : $json->response->docs[$i]->title;
			$venueExternalId = empty($json->response->docs[$i]->venue_config_id) ? null : $json->response->docs[$i]->venue_config_id;
			$venueName = empty($json->response->docs[$i]->venue_name) ? null : $json->response->docs[$i]->venue_name;
			$venueAddress = empty($json->response->docs[$i]->addr1) ? null : $json->response->docs[$i]->addr1;
			$country = empty($json->response->docs[$i]->country) ? null : $json->response->docs[$i]->country;
			$postalCode = empty($json->response->docs[$i]->zip) ? null : $json->response->docs[$i]->zip;
			$defaultDomain = empty($json->response->docs[$i]->defaultDomain) ? "http://stubhub.com" : $json->response->docs[$i]->defaultDomain;
			$venueExternalUrl = empty($json->response->docs[$i]->venueDetailUrlPath) ? null : "http://".$defaultDomain."/".$json->response->docs[$i]->venueDetailUrlPath;
			$latitude = empty($json->response->docs[$i]->latitude) ? null : $json->response->docs[$i]->latitude;
			$longitude = empty($json->response->docs[$i]->longitude) ? null : $json->response->docs[$i]->longitude;
			$timezone = empty($json->response->docs[$i]->jdk_timezone) ? null : $json->response->docs[$i]->jdk_timezone;
			$startTime = empty($json->response->docs[$i]->event_date) ? null : str_replace("T", " ", substr($json->response->docs[$i]->event_date, 0, -1));
			$minPrice = empty($json->response->docs[$i]->minPrice) ? null : $json->response->docs[$i]->minPrice;
			$maxPrice = empty($json->response->docs[$i]->maxPrice) ? null : $json->response->docs[$i]->maxPrice;
			$channel = empty($json->response->docs[$i]->channel) ? null : strtolower($json->response->docs[$i]->channel);
			$eventExternalUrl = empty($json->response->docs[$i]->urlpath) ? null : "http://".$defaultDomain."/".$json->response->docs[$i]->urlpath;
			$imageExternalUrl = empty($json->response->docs[$i]->image_url) ? null : $json->response->docs[$i]->image_url;
			$geoParent = empty($json->response->docs[$i]->geography_parent) ? null : $json->response->docs[$i]->geography_parent;
			$imageExternalUrlLong = empty($json->response->docs[$i]->image_url) ? null : "http://cache1.stubhubstatic.com/data/venue_maps/".$geoParent."/".$json->response->docs[$i]->image_url;

			if(null != $latitude && null != $longitude && null != $userLat && null != $userLong){
				$distance = distanceInMiles($userLat, $userLong, $latitude, $longitude);
			} else{
				$distance = null;
			}
			
			$currentEvent = true;
			
			if(!empty($startTime)){
				date_default_timezone_set($timezone);
				$currentUnixTime = strtotime("now");
				$eventUnixTime = strtotime($startTime);
				
				$currentEvent = ($currentUnixTime > $eventUnixTime) ? false : true;
			}
				
           	// If no externalId is set, don't pull record. Avoids empty 
           	// records from getting pulled.
            if(!empty($externalId) && $currentEvent){
			if(empty($filterCity)  || strtolower($filterCity) == strtolower($city)){
			if(empty($filterState) || strtolower($filterState) == strtolower($state)){
		    if(empty($filterDate)  || $filterDate == $date){
			if(empty($filterDesc)  || (strpos(strtolower($desc), strtolower($filterDesc)) !== FALSE)){
			if(empty($filterRadius) || $distance <= $filterRadius){
	            $gEvent = new gEvent;
	            $gEvent->setExternal_id($externalId);
	            $gEvent->setDatasource("stubhub");
	            $gEvent->setTitle($title);
	            $gEvent->setDescription($desc);
	            $gEvent->setNotes("");

	            $gEvent->setVenue_external_id($venueExternalId);
	            $gEvent->setVenue_name($venueName);
	            $gEvent->setVenue_address($venueAddress);

	            $gEvent->setCountry_name($country);
	            $gEvent->setState_name($state);
	            $gEvent->setCity_name($city);
	            $gEvent->setPostal_code($postalCode);
	            $gEvent->setVenue_external_url($venueExternalUrl);

	            $gEvent->setLatitude($latitude);
	            $gEvent->setLongitude($longitude);
	            $gEvent->setDistance(number_format((float)$distance, 2, '.', ''));
	            
				$gEvent->setTimezone($timezone);
				$gEvent->setTimezone_abbr(convertTimezoneToAbbr($timezone));
	            $gEvent->setStart_time($startTime);   
	            $gEvent->setEnd_time("");
	            $gEvent->setStart_date_month(convertDateToMonthOptions($startTime));
	            $gEvent->setStart_date_day(convertDateToDayOptions($startTime, $timezone));
	            $gEvent->setStart_date_year(convertDateToYearOptions($startTime));
	            $gEvent->setStart_date_time(convertDateToTimeOptions($startTime));

	            $gEvent->setPrice_range($minPrice." - ".$maxPrice);
	            if(substr($gEvent->getPrice_range(), 0, 2 ) === "0 ") {
	            	$gEvent->setIs_free(true);
	            } else if($gEvent->getPrice_range() !== null){
	            	$gEvent->setIs_free(false);
	            }

	            $minorGenre = trim(str_replace("tickets", " ", $channel));
	            $majorGenre = trim(str_replace("tickets", " ", $channel));
	            $gEvent->setMinor_genre(array($minorGenre));
	            $gEvent->setMajor_genre(array($majorGenre));
	                     
	            $gEvent->setEvent_external_url($eventExternalUrl);
				
				
	            // Populate image links found
	            $gEventImages = array();    
	            if(isset($imageExternalUrl)){
	            	$gImage = new gEventImage;
	            	$gImage->setImage_external_url($imageExternalUrlLong);
	            	$gImage->setImage_category("venue");
	            	array_push($gEventImages, $gImage);
	            }
	            $gEvent->setImages($gEventImages);
				
				/*
	            //Populate performers found
	            $gEventPerformers = array();
	            if(isset($json->response->docs[$i]->AttractionName[0])){
	            	$aI = 0;
	            	foreach ($json->response->docs[$i]->AttractionName as $performer) {
		            	if(isset($performer) && null != $performer && $performer != ""){
			            	$gPerformer = new gEventPerformer;
			            	$gPerformer->setPerformer_name($performer);
			            	$gPerformer->setPerformer_external_image_url("http://s1.ticketm.net/tm/en-us".$json->response->docs[$i]->AttractionImage[$aI]);
			            	array_push($gEventPerformers, $gPerformer);
		            	}
		            	$aI++;
	            	}
	            }
	            $gEvent->setPerformers($gEventPerformers);
				*/
				
	            // Push gEvent object onto arraylist of gEvent objects
	            array_push($gEvents, $gEvent);
	        }
			}
			}
			}
			}
			}
			$i++;
		}
		return $gEvents;
	}
?>
