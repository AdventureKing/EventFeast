<?php
	require_once('Cache/Lite.php');
	error_reporting(E_ALL ^ E_STRICT); 
	
	function getCachedContent($url, $anonfunction){
		$options = array(
		'lifeTime' => 86400, //2 hours
		'pearErrorMode' => CACHE_LITE_ERROR_DIE
		);
		$cache = new Cache_Lite($options);

		if ($data = $cache->get($url)) {
			return $data;
		} else { 
			// No valid cache found (you have to make and save the page)
			$data = $anonfunction;
			$cache->save($data);
			return $data;
		}
	}
	
	function googleImageSearch($searchQueryUrl){
		$randomIP = long2ip(rand(0, "4294967295"));
		$url = $searchQueryUrl."&rsz=2&imgsz=medium&userip=$randomIP";
		$data = getCachedContent($searchQueryUrl."1", get_data($url));
		$json = json_decode($data);
			
		$imgUrl = $json->responseData->results[0]->unescapedUrl;
		
		return $imgUrl;
	}
?>