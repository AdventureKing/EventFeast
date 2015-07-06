<?php
	
	function convertDateToYearOptions($date){
		$dateOptions = array();

		// Date Example: 2015-07-15
		if (preg_match("/^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) [0-2]{1}[0-9]{1}:[0-5]{1}[0-9]{1}:[0-5]{1}[0-9]{1}$/",$date))
		{
			$dateTime = explode(" ", $date);
			$dateOnly = $dateTime[0];
			$dateSplit = explode("-", $dateOnly);

			$dateYear = $dateSplit[0];
			$dateYearLong = $dateYear;
			$dateYearShort = substr($dateYear, 2, 2);

			array_push($dateOptions, $dateYearLong);
			array_push($dateOptions, $dateYearShort);

			return $dateOptions;
		}
		
		return $date;
	}

	function convertDateToMonthOptions($date){
		$dateOptions = array();

		// Date Example: 2015-07-15
		if (preg_match("/^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) [0-2]{1}[0-9]{1}:[0-5]{1}[0-9]{1}:[0-5]{1}[0-9]{1}$/",$date))
		{
			$dateTime = explode(" ", $date);
			$dateOnly = $dateTime[0];
			$dateSplit = explode("-", $dateOnly);

			$dateMonth = $dateSplit[1];
			$dateMonthNumber = $dateMonth;
			switch ($dateMonthNumber) {
			    case "01":
			        $dateMonthName = "January";
			        break;
			    case "02":
			        $dateMonthName = "February";
			        break;
			    case "03":
			        $dateMonthName = "March";
			        break;
			    case "04":
			        $dateMonthName = "April";
			        break;
			    case "05":
			        $dateMonthName = "May";
			        break;
			    case "06":
			        $dateMonthName = "June";
			        break;
			    case "07":
			        $dateMonthName = "July";
			        break;
			    case "08":
			        $dateMonthName = "August";
			        break;
			    case "09":
			        $dateMonthName = "September";
			        break;
			    case "10":
			        $dateMonthName = "October";
			        break;
			    case "11":
			        $dateMonthName = "November";
			        break;
			    case "12":
			        $dateMonthName = "December";
			        break;
			    default:
			        $dateMonthName = null;
			}

			$dateMonthNameLong = $dateMonthName;
			$dateMonthNameShort = substr($dateMonthName, 0, 3);
			
			array_push($dateOptions, $dateMonthNumber);
			array_push($dateOptions, $dateMonthNameLong);
			array_push($dateOptions, $dateMonthNameShort);

			return $dateOptions;
		}
		return $date;
	}

	function convertDateToDayOptions($date){
		$dateOptions = array();

		// Date Example: 2015-07-15
		if (preg_match("/^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) [0-2]{1}[0-9]{1}:[0-5]{1}[0-9]{1}:[0-5]{1}[0-9]{1}$/",$date))
		{
			$dateTime = explode(" ", $date);
			$dateOnly = $dateTime[0];
			$dateSplit = explode("-", $dateOnly);

			$dateDay= $dateSplit[2];

			$dateDayNumber = $dateDay;
			date_default_timezone_set("America/Chicago");
			$dateDayLong = (string) date('l', strtotime($dateOnly));
			$dateDayShort = (string) date('D', strtotime($dateOnly));

			array_push($dateOptions, $dateDayNumber);
			array_push($dateOptions, $dateDayLong);
			array_push($dateOptions, $dateDayShort);
			

			return $dateOptions;
		}
		
		return $date;
	}

	function convertDateToTimeOptions($date){
		$dateOptions = array();

		// Date Example: 2015-07-15
		if (preg_match("/^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) [0-2]{1}[0-9]{1}:[0-5]{1}[0-9]{1}:[0-5]{1}[0-9]{1}$/",$date))
		{
			$dateTime = explode(" ", $date);
			$timeOnly = $dateTime[1];
			$timeSplit = explode(":", $timeOnly);

			$timeMilitary = $timeOnly;
			if($timeSplit[0] > 12){
				$hour = $timeSplit[0] - 12;
				$timeNonMilitary = "0".$hour.":".$timeSplit[1].":".$timeSplit[2];
			} else{
				$timeNonMilitary = $timeOnly;
			}
			if($timeSplit[0] > 12){
				$hour = $timeSplit[0] - 12;
				$timeShort = $hour.":".$timeSplit[1]."pm";
			} else if($timeSplit[0] == 12){
				$hour = $timeSplit[0];
				$timeShort = $hour.":".$timeSplit[1]."pm";
			} else{
				$hour = $timeSplit[0];
				str_replace("0","",$hour);
				$timeShort = $hour.":".$timeSplit[1]."am";
			}

			array_push($dateOptions, $timeMilitary);
			array_push($dateOptions, $timeNonMilitary);
			array_push($dateOptions, $timeShort);

			return $dateOptions;
		}
		
		return $date;
	}

?>