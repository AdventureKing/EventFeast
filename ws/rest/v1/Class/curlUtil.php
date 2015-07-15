<?php
	function get_data($url){
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