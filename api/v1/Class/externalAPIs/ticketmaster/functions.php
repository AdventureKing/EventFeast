<?php 
	require_once($projectDir.'Class/gEvent.php');
	require_once($projectDir.'Class/curlUtil.php');

	function ticketmasterAPI_findEvents($queryString, $optionsArray){
		
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
		
		$endpoint_ticketmaster = "http://www.ticketmaster.com/json/search/event";
		$url = "$endpoint_ticketmaster?q=$queryString&rows=50&VenueCity=$filterCity&VenueState=$filterState";
		$data = get_data($url);
		$json = json_decode($data);
		$num = $json->response->numFound;
		
		$gEvents = array();
		$i = 0;

		// Loop through Json Results from CURL resuest
		while ($i < $num) {
			$externalId = empty($json->response->docs[$i]->Id) ? null : $json->response->docs[$i]->Id;
			$city = empty($json->response->docs[$i]->VenueCity) ? null : $json->response->docs[$i]->VenueCity;
			$state = empty($json->response->docs[$i]->VenueState) ? null : $json->response->docs[$i]->VenueState;
			$date = empty($json->response->docs[$i]->PostProcessedData->LocalEventDate) ? null : substr($json->response->docs[$i]->PostProcessedData->LocalEventDate, 0, -15);
			$desc = empty($json->response->docs[$i]->EventName) ? null : $json->response->docs[$i]->EventName;
			
			$title = empty($json->response->docs[$i]->EventName) ? null : $json->response->docs[$i]->EventName;
			$eventInfo = empty($json->response->docs[$i]->EventInfo) ? null : htmlspecialchars($json->response->docs[$i]->EventInfo."\n\n", ENT_QUOTES);
			$eventNotes = empty($json->response->docs[$i]->EventNotes) ? null : (htmlspecialchars($json->response->docs[$i]->EventNotes, ENT_QUOTES));
			$venueExternalId = empty($json->response->docs[$i]->VenueId) ? null : $json->response->docs[$i]->VenueId;
			$venueName = empty($json->response->docs[$i]->VenueName) ? null : $json->response->docs[$i]->VenueName;
			$venueAddress = empty($json->response->docs[$i]->VenueAddress) ? null : $json->response->docs[$i]->VenueAddress;
			$country = empty($json->response->docs[$i]->VenueCountry) ? null : $json->response->docs[$i]->VenueCountry;
			$postalCode = empty($json->response->docs[$i]->VenuePostalCode) ? null : $json->response->docs[$i]->VenuePostalCode;
			$defaultDomain = "http://ticketmaster.com";
			$venueExternalUrl = empty($json->response->docs[$i]->VenueSEOLink) ? null : $defaultDomain.$json->response->docs[$i]->VenueSEOLink;
			$venueLatLongString = empty($json->response->docs[$i]->VenueLatLong) ? null : $json->response->docs[$i]->VenueLatLong;
			$timezone = empty($json->response->docs[$i]->Timezone) ? null : $json->response->docs[$i]->Timezone;
			$startTime = empty($json->response->docs[$i]->PostProcessedData->LocalEventDate) ? null : str_replace("T", " ", substr($json->response->docs[$i]->PostProcessedData->LocalEventDate, 0, -6));
			$priceRange = empty($json->response->docs[$i]->PriceRange) ? null : $json->response->docs[$i]->PriceRange;
			$minPrice = empty($json->response->docs[$i]->minPrice) ? null : $json->response->docs[$i]->minPrice;
			$maxPrice = empty($json->response->docs[$i]->maxPrice) ? null : $json->response->docs[$i]->maxPrice;
			$genre = empty($json->response->docs[$i]->Genre[0]) ? null : $json->response->docs[$i]->Genre;
			$minorGenre = empty($json->response->docs[$i]->MinorGenre[0]) ? null : $json->response->docs[$i]->MinorGenre;
			$majorGenre = empty($json->response->docs[$i]->MajorGenre[0]) ? null : $json->response->docs[$i]->MajorGenre;
			$eventSEOName = empty($json->response->docs[$i]->EventSEOName) ? null : $json->response->docs[$i]->EventSEOName;
			$eventExternalUrl = $defaultDomain."/".$eventSEOName."/event/".$externalId;
			$eventExternalAttractionUrl = empty($json->response->docs[$i]->AttractionSEOLink[0]) ? null : "http://ticketmaster.com".$json->response->docs[$i]->AttractionSEOLink[0];
			
			$currentEvent = true;
			$distance = null;
			
			if(!empty($venueLatLongString)){
				$venueLatLong = explode(",", $venueLatLongString);
				if(null != $venueLatLong[0] && null != $venueLatLong[1] && null != $userLat && null != $userLong){
						$distance = distanceInMiles($userLat, $userLong, $venueLatLong[0], $venueLatLong[1]);
				} 
			}

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
	            $gEvent->setDatasource("ticketmaster");
	            $gEvent->setTitle($title);
	            $gEvent->setDescription($desc);
	            $gEvent->setNotes($eventInfo.$eventNotes);

	            $gEvent->setVenue_external_id($venueExternalId);
	            $gEvent->setVenue_name($venueName);
	            $gEvent->setVenue_address($venueAddress);

	            $gEvent->setCountry_name($country);
	            $gEvent->setState_name($state);
	            $gEvent->setCity_name($city);
	            $gEvent->setPostal_code($postalCode);
	            $gEvent->setVenue_external_url($venueExternalUrl);

				if(!empty($venueLatLongString)){
					$venueLatLong = explode(",", $venueLatLongString);
					$gEvent->setLatitude((float)$venueLatLong[0]);
					$gEvent->setLongitude((float)$venueLatLong[1]);
					
					if(null != $distance){
						$gEvent->setDistance(number_format((float)$distance, 2, '.', ''));
					}
				}
	            
				$gEvent->setTimezone($timezone);
				$gEvent->setTimezone_abbr(convertTimezoneToAbbr($timezone));
	            $gEvent->setStart_time($startTime);   
	            $gEvent->setEnd_time("");
	            $gEvent->setStart_date_month(convertDateToMonthOptions($startTime));
	            $gEvent->setStart_date_day(convertDateToDayOptions($startTime, $timezone));
	            $gEvent->setStart_date_year(convertDateToYearOptions($startTime));
	            $gEvent->setStart_date_time(convertDateToTimeOptions($startTime));

	            $gEvent->setPrice_range($priceRange);
	            if(substr($priceRange, 0, 2 ) === "0 ") {
	            	$gEvent->setIs_free(true);
	            } else if($gEvent->getPrice_range() !== null){
	            	$gEvent->setIs_free(false);
	            }

	            $gEvent->setMinor_genre(array_merge($genre, $minorGenre));
	            $gEvent->setMajor_genre($majorGenre);
	                     
	            $gEvent->setEvent_external_url($eventExternalUrl);

	            // Populate image links found
	            $gEventImages = array();
	            if(!empty($json->response->docs[$i]->AttractionImage[0])){
	            	foreach ($json->response->docs[$i]->AttractionImage as $image) {
		            	if(isset($image) && null != $image && $image != ""){
			            	$gImage = new gEventImage;
			            	$gImage->setImage_external_url("http://s1.ticketm.net/tm/en-us".$image);
			            	$gImage->setImage_category("attraction");
			            	array_push($gEventImages, $gImage);
		            	}
	            	}
	            }         
	            if(!empty($json->response->docs[$i]->VenueImage)){
	            	$gImage = new gEventImage;
	            	$gImage->setImage_external_url("http://s1.ticketm.net/tm/en-us".$json->response->docs[$i]->VenueImage);
	            	$gImage->setImage_category("venue");
	            	array_push($gEventImages, $gImage);
	            }
	            $gEvent->setImages($gEventImages);

	            //Populate performers found
	            $gEventPerformers = array();
				
	            if(!empty($json->response->docs[$i]->AttractionName[0])){
	            	$aI = 0;
	            	foreach ($json->response->docs[$i]->AttractionName as $performer) {
		            	if(!empty($performer)){
			            	$gPerformer = new gEventPerformer;
			            	$gPerformer->setPerformer_name($performer);
							$attractionImageUrl = empty($json->response->docs[$i]->AttractionImage[$aI]) ? null : $json->response->docs[$i]->AttractionImage[$aI];
							if(!empty($attractionImageUrl)){
								$gPerformer->setPerformer_external_image_url("http://s1.ticketm.net/tm/en-us".$json->response->docs[$i]->AttractionImage[$aI]);
							}
			            	array_push($gEventPerformers, $gPerformer);
		            	}
		            	$aI++;
	            	}
	            }
	            $gEvent->setPerformers($gEventPerformers);
				
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
