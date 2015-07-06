<?php 
	require_once('../../classes/gEvent.php');
	require_once('../../classes/curlUtil.php');

	$q = $_GET['q'];
	$optionsArray['city'] = $_GET['city'];

	ticketmasterAPI_findEvents($q, $optionsArray);
	
	function ticketmasterAPI_findEvents($queryString, $optionsArray){
		
		// Filter Parameters
		$q = $queryString;
		$filterCity = $optionsArray['city'];
		$filterState = $optionsArray['state'];
		$filterDate = $optionsArray['date'];
		$filterSource = $optionsArray['source'];
		$filterDesc = $optionsArray['desc'];
	
		$data = get_data("http://www.ticketmaster.com/json/search/event?q=$queryString");
		$json = json_decode($data);
		$num = $json->response->numFound;
		
		$gEvents = array();
		$i = 0;

		// Loop through Json Results from CURL resuest
		while ($i < $num) {
            $externalId = $json->response->docs[$i]->Id;

           	// If no externalId is set, don't pull record. Avoids empty 
           	// records from getting pulled.
            if(isset($externalId)){
	            $gEvent = new gEvent;
	            $gEvent->setExternal_id($externalId);
	            $gEvent->setDatasource("ticketmaster");
	            $gEvent->setTitle($json->response->docs[$i]->EventName);
	            $gEvent->setDescription($json->response->docs[$i]->EventName);
	            $gEvent->setNotes($json->response->docs[$i]->EventNotes);

	            $gEvent->setVenue_external_id($json->response->docs[$i]->VenueId);
	            $gEvent->setVenue_name($json->response->docs[$i]->VenueName);
	            $gEvent->setVenue_address($json->response->docs[$i]->VenueAddress);

	            $gEvent->setCountry_name($json->response->docs[$i]->VenueCountry);
	            $gEvent->setState_name($json->response->docs[$i]->VenueState);
	            $gEvent->setCity_name($json->response->docs[$i]->VenueCity);
	            $gEvent->setPostal_code($json->response->docs[$i]->VenuePostalCode);
	            $gEvent->setVenue_external_url("http://ticketmaster.com".$json->response->docs[$i]->VenueSEOLink);

	            $venueLatLong = explode(",", $json->response->docs[$i]->VenueLatLong);
	            $gEvent->setLatitude($venueLatLong[0]);
	            $gEvent->setLongitude($venueLatLong[1]);
	            
	            $gEvent->setStart_time(str_replace("T", " ", substr($json->response->docs[$i]->PostProcessedData->LocalEventDate, 0, -6)));   
	            $gEvent->setEnd_time("");
	            $gEvent->setStart_date_month(convertDateToMonthOptions($gEvent->getStart_time()));
	            $gEvent->setStart_date_day(convertDateToDayOptions($gEvent->getStart_time()));
	            $gEvent->setStart_date_year(convertDateToYearOptions($gEvent->getStart_time()));
	            $gEvent->setStart_date_time(convertDateToTimeOptions($gEvent->getStart_time()));

	            $gEvent->setPrice_range($json->response->docs[$i]->PriceRange);
	            if(substr($gEvent->getPrice_range(), 0, 2 ) === "0 ") {
	            	$gEvent->setIs_free(true);
	            } else if($gEvent->getPrice_range() !== null){
	            	$gEvent->setIs_free(false);
	            }

	            $minorGenre = (isset($json->response->docs[$i]->Genre[0])) ? $json->response->docs[$i]->Genre : $json->response->docs[$i]->MinorGenre;
	            $majorGenre = $json->response->docs[$i]->MajorGenre;
	            $gEvent->setMinor_genre($minorGenre);
	            $gEvent->setMajor_genre($majorGenre);
	                     
	            $gEvent->setEvent_external_url("http://ticketmaster.com".$json->response->docs[$i]->AttractionSEOLink[0]);

	            // Populate image links found
	            $gEventImages = array();
	            if(isset($json->response->docs[$i]->AttractionImage[0])){
	            	foreach ($json->response->docs[$i]->AttractionImage as $image) {
		            	if(isset($image) && null != $image && $image != ""){
			            	$gImage = new gEventImage;
			            	$gImage->setImage_external_url("http://s1.ticketm.net/tm/en-us".$image);
			            	$gImage->setImage_category("attraction");
			            	array_push($gEventImages, $gImage);
		            	}
	            	}
	            }         
	            if(isset($json->response->docs[$i]->VenueImage)){
	            	$gImage = new gEventImage;
	            	$gImage->setImage_external_url("http://s1.ticketm.net/tm/en-us".$json->response->docs[$i]->VenueImage);
	            	$gImage->setImage_category("venue");
	            	array_push($gEventImages, $gImage);
	            }
	            $gEvent->setImages($gEventImages);

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

	            // Push gEvent object onto arraylist of gEvent objects
	            array_push($gEvents, $gEvent);
			}
			$i++;
		}
		var_dump(json_encode($gEvents, JSON_UNESCAPED_SLASHES));
		return $gEvents;
	}
?>