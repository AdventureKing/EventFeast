<?php
	require_once($projectDir.'Class/gEvent.php');
	require_once($projectDir.'Class/curlUtil.php');
	
	function eventfulAPI_findEvents($queryString, $optionsArray){
		$app_key = 'NzRqz6hgC5t3ZR8B';

		// Filter Parameters
		$q = urlencode(strtolower($queryString));
		$userLat = $optionsArray['userLat'];
		$userLong = $optionsArray['userLng'];
		$filterCity = $optionsArray['city'];
		$filterState = $optionsArray['state'];
		$filterDate = $optionsArray['date'];
		$filterRadius = $optionsArray['radius'];
		
		if($q == $filterCity){
			$q == null;
		}
		
		// Just do a simple lookup
		$ev_noauth = new Services_Eventful($app_key);
		$args = array(
		'keywords'   => "$q",
		'location'   => "$filterCity, $filterState",
		'within'     => "$filterRadius",
		'units'      => "mi",
		'sort_order' => "date",
		'page_size'  => 100
		);
		$data = $ev_noauth->call('events/search', $args, "json");
		
		$num = $data['page_size'];
		$gEvents = array();
		$i = 0;
		
		// Loop through Json Results from CURL resuest
		while ($i < $num) {
            $externalId = empty($data['events']['event'][$i]['id']) ? null : $data['events']['event'][$i]['id'];
			$city = empty($data['events']['event'][$i]['city_name']) ? null : $data['events']['event'][$i]['city_name'];
			$state = empty($data['events']['event'][$i]['region_abbr']) ? null : $data['events']['event'][$i]['region_abbr'];
			$date = empty($data['events']['event'][$i]['start_time']) ? null : $data['events']['event'][$i]['start_time'];
			$desc = empty($data['events']['event'][$i]['description']) ? null : $data['events']['event'][$i]['description'];
			
			$title = empty($data['events']['event'][$i]['title']) ? null : $data['events']['event'][$i]['title'];
			$venueExternalId = empty($data['events']['event'][$i]['venue_id']) ? null : $data['events']['event'][$i]['venue_id'];
			$venueName = empty($data['events']['event'][$i]['venue_name']) ? null : $data['events']['event'][$i]['venue_name'];
			$venueAddress = empty($data['events']['event'][$i]['venue_address']) ? null : $data['events']['event'][$i]['venue_address'];
			$country = empty($data['events']['event'][$i]['country_abbr']) ? null : $data['events']['event'][$i]['country_abbr'];
			$postalCode = empty($data['events']['event'][$i]['postal_code']) ? null : $data['events']['event'][$i]['postal_code'];
			$defaultDomain = empty($data['events']['event'][$i]['default_domain']) ? "http://eventful.com" : $data['events']['event'][$i]['default_domain'];
			$venueExternalUrl = empty($data['events']['event'][$i]['venue_url']) ? null : $data['events']['event'][$i]['venue_url'];
			$latitude = empty($data['events']['event'][$i]['latitude']) ? null : $data['events']['event'][$i]['latitude'];
			$longitude = empty($data['events']['event'][$i]['longitude']) ? null : $data['events']['event'][$i]['longitude'];
			$timezone = empty($data['events']['event'][$i]['olson_path']) ? null : $data['events']['event'][$i]['olson_path'];
			$startTime = empty($data['events']['event'][$i]['start_time']) ? null : $data['events']['event'][$i]['start_time'];
			$endTime = empty($data['events']['event'][$i]['stop_time']) ? null : $data['events']['event'][$i]['stop_time'];
			$minPrice = null;
			$maxPrice = null;
			$channel = null;
			$eventExternalUrl = empty($data['events']['event'][$i]['url']) ? null : $data['events']['event'][$i]['url'];
			$imageExternalUrl = empty($data['events']['event'][$i]['image']) ? null : $data['events']['event'][$i]['image'];
			$geoParent = empty($data['events']['event'][$i]['geocode_type']) ? null : $data['events']['event'][$i]['geocode_type'];

			$currentEvent = true;

			if(!empty($startTime)){
				date_default_timezone_set($timezone);
				$currentUnixTime = strtotime("now");
				$eventUnixTime = strtotime($startTime);
				
				$currentEvent = ($currentUnixTime > $eventUnixTime) ? false : true;
			}

			if(null != $latitude && null != $longitude && null != $userLat && null != $userLong){
				$distance = distanceInMiles($userLat, $userLong, $latitude, $longitude);
			} else{
				$distance = null;
			}
				
            if(!empty($externalId) && $currentEvent){
			if(empty($filterRadius) || ($distance <= $filterRadius && $distance > 0)){
				
	            $gEvent = new gEvent;
	            $gEvent->setExternal_id($externalId);
	            $gEvent->setDatasource("eventful");
	            $gEvent->setTitle($title);
	            $gEvent->setDescription($title);
	            $gEvent->setNotes($desc);
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
	            $gEvent->setEnd_time($endTime);
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
	            $minorGenre = array();
	            $majorGenre = array();
	            $gEvent->setMinor_genre($minorGenre);
	            $gEvent->setMajor_genre($majorGenre);
	                     
	            $gEvent->setEvent_external_url($eventExternalUrl);
				
	            // Populate image links found
	            $gEventImages = array();    
				
	            if(isset($imageExternalUrl)){
			$image = $data['events']['event'][$i]['image']['medium'];
			//var_dump($image);
			//if(!empty($image['medium'])){
			$gImage = new gEventImage;
			$gImage->setImage_external_url($image['url']);
			$gImage->setImage_height($image['height']);
			$gImage->setImage_width($image['width']);
			$gImage->setImage_category("attraction");
			array_push($gEventImages, $gImage);
			//}
					
	            }
	            $gEvent->setImages($gEventImages);
				
	            //Populate performers found
	            $gEventPerformers = array();
	            if(isset($data['events']['event'][$i]['performers']['performer'])){
	            	foreach ($data['events']['event'][$i]['performers']['performer'] as $performer) {
		            	if(isset($performer) && isset($performer['id'])){
			            	$gPerformer = new gEventPerformer;
							$gPerformer->setPerformer_external_id($performer['id']);
			            	$gPerformer->setPerformer_name($performer['name']);
			            	$gPerformer->setPerformer_external_url($performer['url']);
							$gPerformer->setPerformer_short_bio($performer['short_bio']);
			            	array_push($gEventPerformers, $gPerformer);
		            	}
	            	}
	            }
	            $gEvent->setPerformers($gEventPerformers);
				
				
	            // Push gEvent object onto arraylist of gEvent objects
	            array_push($gEvents, $gEvent);
	        }
			}
			$i++;
		}

		return $gEvents;
	}

?>
