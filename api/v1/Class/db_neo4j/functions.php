<?php 
	require_once($projectDir.'Class/gEvent.php');
	require_once($projectDir.'Class/curlUtil.php');

	function neo4j_findEvents($queryString, $optionsArray, $data){
		
		// Function Filter Parameters
		$userLat = $optionsArray['userLat'];
		$userLong = $optionsArray['userLng'];
		$filterRadius = $optionsArray['radius'];

		//$dbNeo = new DbHandlerNeo();
		
		$num = count($data);
		$json = json_encode($data, JSON_FORCE_OBJECT | JSON_UNESCAPED_SLASHES);
		$json = json_decode($json);
		//echo $json[0];
		
		$gEvents = array();
		$i = 0;

		// Loop through Json Results from Neo4j resuest
		foreach($json as $jObject) {
			$internal_id = empty($jObject->internal_id) ? null : $jObject->internal_id;
			$external_id = empty($jObject->external_id) ? null : $jObject->external_id;
			$datasource = empty($jObject->datasource) ? null : $jObject->datasource;
			$event_external_url = empty($jObject->event_external_url) ? null : $jObject->event_external_url;
			$title = empty($jObject->title) ? null : $jObject->title;
			$title = str_replace('"', "'", $title);
			$description = empty($jObject->description) ? null : $jObject->description;
			$description = str_replace('"', "'", $description);
			$notes = empty($jObject->notes) ? null : $jObject->notes;
			$notes = str_replace('"', "'", $notes);
			$timezone = empty($jObject->timezone) ? null : $jObject->timezone;
			$timezone_abbr = empty($jObject->timezone_abbr) ? null : $jObject->timezone_abbr;
			$start_time = empty($jObject->start_time) ? null : $jObject->start_time;
			$end_time = empty($jObject->end_time) ? null : $jObject->end_time;
			$start_date_month = empty($jObject->start_date_month) ? array() : json_decode($jObject->start_date_month, true);
			$start_date_day = empty($jObject->start_date_day) ? array() : json_decode($jObject->start_date_day, true);
			$start_date_year = empty($jObject->start_date_year) ? array() : json_decode($jObject->start_date_year, true);
			$start_date_time = empty($jObject->start_date_time) ? array() : json_decode($jObject->start_date_time, true);
			$end_date_month = empty($jObject->end_date_month) ? array() : json_decode($jObject->end_date_month, true);
			$end_date_day = empty($jObject->end_date_day) ? array() : json_decode($jObject->end_date_day, true);
			$end_date_year = empty($jObject->end_date_year) ? array() : json_decode($jObject->end_date_year, true);
			$end_date_time = empty($jObject->end_date_time) ? array() : json_decode($jObject->end_date_time, true);

			$venue_external_id = empty($jObject->venue_external_id) ? null : $jObject->venue_external_id;
			$venue_external_url = empty($jObject->venue_external_url) ? null : $jObject->venue_external_url;
			$venue_name = empty($jObject->venue_name) ? null : $jObject->venue_name;
			$venue_display = empty($jObject->venue_display) ? null : $jObject->venue_display;
			$venue_address = empty($jObject->venue_address) ? null : $jObject->venue_address;
			$state_name = empty($jObject->state_name) ? null : $jObject->state_name;
			$city_name = empty($jObject->city_name) ? null : $jObject->city_name;
			$postal_code = empty($jObject->postal_code) ? null : $jObject->postal_code;
			$country_name = empty($jObject->country_name) ? null : $jObject->country_name;
			$all_day = empty($jObject->all_day) ? null : $jObject->all_day;
			$price_range = empty($jObject->price_range) ? null : $jObject->price_range;
			$is_free = empty($jObject->is_free) ? null : $jObject->is_free;

			$major_genre = empty($jObject->major_genre) ? array() : json_decode($jObject->major_genre, true);
			$minor_genre = empty($jObject->minor_genre) ? array() : json_decode($jObject->minor_genre, true);
			$latitude = empty($jObject->latitude) ? null : $jObject->latitude;
			$longitude = empty($jObject->longitude) ? null : $jObject->longitude;

			$performers = empty($jObject->performers) ? array() : json_decode($jObject->performers, true);
			$images = empty($jObject->images) ? array() : json_decode($jObject->images, true);

			$currentEvent = true;
			
			$distance = 0.00;
			if(!empty($latitude) && !empty($longitude)){
				if(null != $userLat && null != $userLong){
					$distance = distanceInMiles($userLat, $userLong, $latitude, $longitude);
				} 
			}
			
			if(!empty($start_time)){
				date_default_timezone_set($timezone);
				$currentUnixTime = strtotime("now");
				$eventUnixTime = strtotime($start_time);
				
				$currentEvent = ($currentUnixTime > $eventUnixTime) ? false : true;
			}
			
           	// If no externalId is set, don't pull record. Avoids empty 
           	// records from getting pulled.
            if($currentEvent){
			if(empty($filterRadius) || ($distance <= $filterRadius && $distance > 0)){
	            $gEvent = new gEvent;
	            $gEvent->setExternal_id($external_id);
	            $gEvent->setDatasource($datasource);
	            $gEvent->setTitle($title);
	            $gEvent->setDescription($description);
	            $gEvent->setNotes($notes);

	            $gEvent->setVenue_external_id($venue_external_id);
	            $gEvent->setVenue_name($venue_name);
	            $gEvent->setVenue_address($venue_address);

	            $gEvent->setCountry_name($country_name);
	            $gEvent->setState_name($state_name);
	            $gEvent->setCity_name($city_name);
	            $gEvent->setPostal_code($postal_code);
	            $gEvent->setVenue_external_url($venue_external_url);

				if(!empty($latitude) && !empty($longitude)){
					$gEvent->setLatitude((float)$latitude);
					$gEvent->setLongitude((float)$longitude);
					
					$gEvent->setDistance(number_format((float)$distance, 2, '.', ''));
				}
	            
				$gEvent->setTimezone($timezone);
				$gEvent->setTimezone_abbr($timezone_abbr);
	            $gEvent->setStart_time($start_time);   
	            $gEvent->setEnd_time($end_time);
	            $gEvent->setStart_date_month($start_date_month);
	            $gEvent->setStart_date_day($start_date_day);
	            $gEvent->setStart_date_year($start_date_year);
	            $gEvent->setStart_date_time($start_date_time);

	            $gEvent->setEnd_date_month($end_date_month);
	            $gEvent->setEnd_date_day($end_date_day);
	            $gEvent->setEnd_date_year($end_date_year);
	            $gEvent->setEnd_date_time($end_date_time);

	            $gEvent->setPrice_range($price_range);
	            $gEvent->setIs_free($is_free);

	            $gEvent->setMinor_genre($minor_genre);
	            $gEvent->setMajor_genre($major_genre);
	                     
	            $gEvent->setEvent_external_url($event_external_url);

	            // Populate image links found  
	            $gEvent->setImages($images);

	            //Populate performers found
	            $gEvent->setPerformers($performers);
				
	            // Push gEvent object onto arraylist of gEvent objects
	            array_push($gEvents, $gEvent);
	        }
			}
			//$i++;
		}

		return $gEvents;
	}
?>