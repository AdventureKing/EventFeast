<?php
	// Get the longitude and latitude given an address, city, state etc.
	function getLatLongFromAddress($address){
		$address = urlencode($address);
		$details_url = "http://maps.googleapis.com/maps/api/geocode/json?address=".$address."&key=AIzaSyAehpaOE86x5u7jBMa3TlhjzDwZ4_6rU68";
		$data = json_decode(get_data($details_url), true);

		// If Status Code is ZERO_RESULTS, OVER_QUERY_LIMIT, REQUEST_DENIED or INVALID_REQUEST
		if ($data['status'] != 'OK') {
			return null;
		}

		$latLng = $data['results'][0]['geometry']['location'];

		$lat = $latLng['lat'];
		$lng = $latLng['lng'];
		$response = array();
		$response['latitude'] = $lat;
		$response['longitude'] = $lng;

		return $response;
	}
	
	function getLatLongFromAddressGeoCoder($address){
		$addressExp = explode(",", $address);
		if(count($addressExp) == 3){
			$addr = urlencode($addressExp[0]);
			$city = urlencode($addressExp[1]);
			$stateZip = explode(" ",$addressExp[2]);
			$state = urlencode($stateZip[0]);
			$zip = urlencode($stateZip[1]);

			//$address = urlencode($address);
			$url = "http://geocoder.maplarge.com/geocoder/json?address=$addr&city=$city&state=$state&zip=$zip&country=USA";
			
			
			//$details_url = "http://maps.googleapis.com/maps/api/geocode/json?address=".$address."&key=AIzaSyAehpaOE86x5u7jBMa3TlhjzDwZ4_6rU68";
			$data = getCachedContent($url, get_data($url));
			$data = json_decode($data, true);

			// If Status Code is ZERO_RESULTS, OVER_QUERY_LIMIT, REQUEST_DENIED or INVALID_REQUEST
			if (!$data['lat']) {
				return null;
			}

			$lat = $data['lat'];
			$lng = $data['lng'];
			$response = array();
			$response['latitude'] = $lat;
			$response['longitude'] = $lng;
		} else{
			$response['latitude'] = 0.00;
		    $response['longitude'] = 0.00;
		}
		return $response;
	}

	// Returns the distance between two points in Miles
	function distanceInMiles($lat_a, $lng_a, $lat_b, $lng_b){
        $earthRadius = 3958.75;
        $latDiff = deg2rad($lat_b - $lat_a);
        $lngDiff = deg2rad($lng_b - $lng_a);
        $a = sin($latDiff / 2) * 
             sin($latDiff / 2) +
             cos(deg2rad($lat_a)) *
             cos(deg2rad($lat_b)) *
             sin($lngDiff / 2) * 
             sin($lngDiff / 2);
        $c = 2 * atan2(sqrt($a), sqrt(1 - $a));
        $distance = $earthRadius * $c;
        $meterConversion = 1609;

        return ($distance);
    }

    // Returns the distance between two points in the specified
    // unit. Miles is default 'M', or 'K' (kilometers) or 'N' (nautical miles)
    function distance($lat1, $lon1, $lat2, $lon2, $unit) {

		$theta = $lon1 - $lon2;
		$dist = sin(deg2rad($lat1)) * sin(deg2rad($lat2)) +  cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * cos(deg2rad($theta));
		$dist = acos($dist);
		$dist = rad2deg($dist);
		$miles = $dist * 60 * 1.1515;
		$unit = strtoupper($unit);

		if ($unit == "K") {
			return ($miles * 1.609344);
		} else if ($unit == "N") {
			return ($miles * 0.8684);
		} else {
			return $miles;
		}
	}
?>