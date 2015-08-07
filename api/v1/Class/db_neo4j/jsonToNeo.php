<?php
	error_reporting(E_ALL ^ E_NOTICE);

	class JsonToNeo{
		
		public function generateInsertCypher($gEvent){
			$event = $gEvent;
			$query = array();
			
			$eventParams = [
			'eid' => $event->external_id, 
			'ds' => $event->datasource,
			'eeu' => $event->event_external_url,
			'tit' => $event->title,
			'des' => $event->description,
			'not' => $event->notes,
			'tz' => $event->timezone,
			'tza' => $event->timezone_abbr,
			'st' => $event->start_time,
			'et' => $event->end_time,
			'sdm' => json_encode($event->start_date_month, JSON_FORCE_OBJECT | JSON_UNESCAPED_SLASHES),
			'sdd' => json_encode($event->start_date_day, JSON_FORCE_OBJECT | JSON_UNESCAPED_SLASHES),
			'sdy' => json_encode($event->start_date_year, JSON_FORCE_OBJECT | JSON_UNESCAPED_SLASHES),
			'sdt' => json_encode($event->start_date_time, JSON_FORCE_OBJECT | JSON_UNESCAPED_SLASHES),
			'edm' => json_encode($event->end_date_month, JSON_FORCE_OBJECT | JSON_UNESCAPED_SLASHES),
			'edd' => json_encode($event->end_date_day, JSON_FORCE_OBJECT | JSON_UNESCAPED_SLASHES),
			'edy' => json_encode($event->end_date_year, JSON_FORCE_OBJECT | JSON_UNESCAPED_SLASHES),
			'edt' => json_encode($event->end_date_time, JSON_FORCE_OBJECT | JSON_UNESCAPED_SLASHES),
			'vei' => $event->venue_external_id,
			'veu' => $event->venue_external_url,
			'vn' => $event->venue_name,
			'vd' => $event->venue_display,
			'va' => $event->venue_address,
			'sn' => $event->state_name,
			'cn' => $event->city_name,
			'pc' => $event->postal_code,
			'con' => $event->country_name,
			'ad' => $event->all_day,
			'pr' => $event->price_range,
			'if' => $event->is_free,
			'mag' => json_encode($event->major_genre, JSON_FORCE_OBJECT | JSON_UNESCAPED_SLASHES),
			'mig' => json_encode($event->minor_genre, JSON_FORCE_OBJECT | JSON_UNESCAPED_SLASHES),
			'lat' => $event->latitude,
			'lon' => $event->longitude,
			'per' => json_encode($event->performers, JSON_FORCE_OBJECT | JSON_UNESCAPED_SLASHES),
			'img' => json_encode($event->images, JSON_FORCE_OBJECT | JSON_UNESCAPED_SLASHES)
			];

			$eventQuery = "
			CREATE (e:Event 
				{ 
				external_id: {eid},
				datasource: {ds},
				event_external_url: {eeu},
				title: {tit},
				description: {des},
				notes: {not},
				timezone: {tz},
				timezone_abbr: {tza},
				start_time: {st},
				end_time: {et},
				start_date_month: {sdm},
				start_date_day: {sdd},
				start_date_year: {sdy},
				start_date_time: {sdt},
				end_date_month: {edm},
				end_date_day: {edd},
				end_date_year: {edy},
				end_date_time: {edt},
				venue_external_id: {vei},
				venue_external_url: {veu},
				venue_name: {vn},
				venue_display: {vd},
				venue_address: {va},
				state_name: {sn},
				city_name: {cn},
				postal_code: {pc},
				country_name: {con},
				all_day: {ad},
				price_range: {pr},
				is_free: {if},
				major_genre: {mag},
				minor_genre: {mig},
				latitude: {lat},
				longitude: {lon},
				performers: {per},
				images: {img}
			})
			";

			// Remove tabs and newline characters
			$eventQuery = str_replace("\n", "", $eventQuery);
			$eventQuery = str_replace("\t", "", $eventQuery);

			$query['query'] = $eventQuery;
			$query['params'] = $eventParams;

			return $query;
		}

		public function jsonEventsToGEventObjects($jsonObject){
			$json = json_decode($jsonObject);

			/**
			 * Loop through each event in the json array
			 * and create a gEvent object and add to our
			 * array to return back to caller.
			 * @var array
			 */
			$gEvents = array();
			foreach($json as $event){
				$gEvent = new gEvent();

				$gEvent->setInternal_Id($event->internal_Id);
				$gEvent->setExternal_Id($event->external_id);
				$gEvent->setDatasource($event->datasource);
				$gEvent->setEvent_external_url($event->event_external_url);
				$gEvent->setTitle($event->title);
				$gEvent->setDescription($event->description);
				$gEvent->setNotes($event->notes);
				$gEvent->setTimezone($event->timezone);
				$gEvent->setTimezone_abbr($event->timezone_abbr);
				$gEvent->setStart_time($event->start_time);
				$gEvent->setEnd_time($event->end_time);

				$gEvent->setStart_date_month($event->start_date_month);
				$gEvent->setStart_date_day($event->start_date_day);
				$gEvent->setStart_date_year($event->start_date_year);
				$gEvent->setStart_date_time($event->start_date_time);

				$gEvent->setEnd_date_month($event->end_date_month);
				$gEvent->setEnd_date_day($event->end_date_day);
				$gEvent->setEnd_date_year($event->end_date_year);
				$gEvent->setEnd_date_time($event->end_date_time);

				$gEvent->setVenue_external_id($event->venue_external_id);
				$gEvent->setVenue_external_url($event->venue_external_url);
				$gEvent->setVenue_name($event->venue_external_name);
				$gEvent->setVenue_display($event->venue_display);
				$gEvent->setVenue_address($event->venue_address);
				$gEvent->setState_name($event->state_name);
				$gEvent->setCity_name($event->city_name);
				$gEvent->setPostal_code($event->postal_code);
				$gEvent->setCountry_name($event->country_name);
				$gEvent->setAll_day($event->all_day);
				$gEvent->setPrice_range($event->price_range);
				$gEvent->setIs_free($event->is_free);
				$gEvent->setMajor_genre($event->major_genre);
				$gEvent->setMinor_genre($event->minor_genre);
				$gEvent->setLatitude($event->latitude);
				$gEvent->setLongitude($event->longitude);

				$performersArr = array();
				foreach($event->performers as $performer){
					$gEventPerformer = new gEventPerformer();
					$gEventPerformer->setPerformer_name($performer->name);
					$gEventPerformer->setPerformer_external_id($performer->external_id);
					$gEventPerformer->setPerformer_external_url($performer->external_url);
					$gEventPerformer->setPerformer_short_bio($performer->short_bio);
					$gEventPerformer->setPerformer_external_image_url($performer->external_image_url);

					array_push($performersArr, $gEventPerformer);
				}
				$gEvent->setPerformers($performersArr);

				$imagesArr = array();
				foreach($event->images as $image){
					$gEventImage = new gEventImage();
					$gEventImage->setImage_category($image->image_category);
					$gEventImage->setImage_height($image->image_height);
					$gEventImage->setImage_width($image->image_width);
					$gEventImage->setImage_external_url($image->image_external_url);

					array_push($imagesArr, $gEventImage);
				}
				$gEvent->setImages($imagesArr);

				// push event object onto event array stack
				array_push($gEvents, $gEvent);
			}

			return $gEvents;
		}

	}

?>