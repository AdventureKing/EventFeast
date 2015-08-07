<?php
	ini_set('display_errors',1);
	ini_set('display_startup_errors',1);
	error_reporting(-1);
	
	function doYelpParsing(){

		// Create DOM from URL or file
		$html = file_get_html('http://www.yelp.com/events/san-antonio-tx-us/browse?sort_by=upcoming&audience=everybody');
		//var_dump($html);
		$eventResults = $html->find('div[id=browse_events_results]')[0];
		echo $eventResults;
		
		/*
		foreach($eventResults->find('div[class=media-story]') as $record ){
			$name = $record->find('span[itemprop=name]')[0]->plaintext;
			$desc = trim($record->find('p[class=event_desc]')[0]->plaintext, " ");
			
			$datetime = explode('T', trim($record->find('meta[itemprop=startDate]')[0]->content, " "));
			$date = $datetime[0];
			$time = (isset($datetime[1]) == true) ? explode('-', $datetime[1]) : null;
			$startTime = (isset($time[0]) == true) ? $time[0] : null;
			
			$venueBiz =  trim($record->find('a[href^=/biz/]')[0]->plaintext, " ,");
			$venueBizURL = trim($record->find('a[href^=/biz/]')[0]->href);
			
			echo "Name: ".$name."</br>";
			echo 'Desc: '.$desc.'</br>';
			echo 'Date: '.$date.'</br>';
			echo 'Start Time: '.$startTime.'</br>';
			echo 'Venue: '.$venueBiz.'</br>';
			echo "URL: <a href='http://yelp.com".$venueBizURL."'>Venue Business URL</a></br>";
			echo "</br>";
		}

		$paginationTable = $html->find('table[class=fs_pagination_controls]')[0];
		$paginationRange = $paginationTable->find('td[class=range-of-total]')[0];
		$pageRangeArray = explode(" ", trim($paginationRange->plaintext));

		$pageCurrentRec = $pageRangeArray[0];
		$pageLastRec = $pageRangeArray[2];
		$pageRecCount = $pageLastRec;
		$pageTotalRecords = $pageRangeArray[4];

		echo "<b>Page Range: $pageCurrentRec to $pageLastRec ($pageTotalRecords records)</b></br></br>";
		echo "<hr></hr>";

		$iterator = $pageLastRec;

		while($iterator <= $pageTotalRecords && $iterator <= 20){
			// Create DOM from URL or file
			$html = file_get_html("http://www.yelp.com/events/san-antonio-tx-us/browse?sort_by=upcoming&audience=everybody&start=$iterator");
			$eventResults = $html->find('div[id=browse_events_results]')[0];

			foreach($eventResults->find('div[class=media-story]') as $record ){
				$name = $record->find('span[itemprop=name]')[0]->plaintext;
				$desc = trim($record->find('p[class=event_desc]')[0]->plaintext, " ");
				
				$datetime = explode('T', trim($record->find('meta[itemprop=startDate]')[0]->content, " "));
				$date = $datetime[0];
				$time = (isset($datetime[1]) == true) ? explode('-', $datetime[1]) : null;
				$startTime = (isset($time[0]) == true) ? $time[0] : null;
				
				echo "Name: ".$name."</br>";
				echo 'Desc: '.$desc.'</br>';
				echo 'Date: '.$date.'</br>';
				echo 'Start Time: '.$startTime.'</br>';
				echo "</br>";
			}
			$startRange = $iterator + 1;
			$endRange = $iterator + $pageRecCount;
			
			echo "<b>Page Range: $startRange to $endRange ($pageTotalRecords records)</b></br></br>";
			echo "<hr></hr>";
			$iterator = $iterator + $pageRecCount;
		}
		*/
		return true;
	}

	function get_data2($url)
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