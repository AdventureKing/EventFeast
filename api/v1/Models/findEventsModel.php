<?php

	class findEventsModel{
		private $app;
		private $eventDesc;

		private $href;
		private $result;
		private $message;
		private $numFound;
		private $offset;
		private $limit;
		private $first;
		private $previous;
		private $next;
		private $last;
		private $items;
		private $jsonWithoutChecksum;
		private $jsonWithChecksum;

		function __construct($eventDesc, $app) {
	       $this->runEvents($eventDesc, $app);
	   	}

	   	public function runEvents($eventDesc, $app){
	   		$this->app = $app;
	   		$this->eventDesc = $eventDesc;
	   		$this->generateItems();
			$this->determineHref();
	   		$this->determineResult();
	   		$this->determineMessage();
	   		$this->determineNumFound();
			$this->determinePagination();
	   		$this->generateJson();
	   	}

		public function generateItems(){
			$filters['userLat'] = $this->app->request->params('userLat');
			$filters['userLng'] = $this->app->request->params('userLng');
			$filters['city'] = $this->app->request()->params('city');
			$filters['state'] = $this->app->request()->params('state');
			$filters['date'] = $this->app->request()->params('date');
			$filters['desc'] = $this->app->request()->params('desc');
			$filters['sources'] = $this->app->request()->params('sources');
			$filters['radius'] = $this->app->request()->params('radius');

			$searchArray = array( 
				'city' => $filters['city'],
				'state' => $filters['state'],
				'date' => $filters['date'],
				'desc' => $filters['desc'],
				'sources' => $filters['sources'],
				'radius' => $filters['radius']
				);
			$searchJson = json_encode($searchArray, JSON_FORCE_OBJECT);
			$dbNeo = new DBHandlerNeo();
			$eventSearchRequestId = $dbNeo->isEventSearchRequestExist($searchJson);

			if($eventSearchRequestId !== null){
				$events = $dbNeo->getEventsByEsrId($eventSearchRequestId);
				if($events != null){
					echo "we have saved events!";
					var_dump($events);
				}
				return;
			}

			if(!empty($filters['sources'])){
				$sourceArr = explode(",",$filters['sources']);
				$stubhub_results = array();
				$ticketmaster_results = array();
				$eventful_results = array();
				
				foreach($sourceArr as $source){
					if(strcasecmp($source, "stubhub") == 0){ 
						$saveString = "stubhub".$this->eventDesc.implode(".",$filters);
						$stubhub_results = getCachedContent($saveString, stubhubAPI_findEvents($this->eventDesc, $filters));			
					}
					if(strcasecmp($source, "ticketMaster") == 0){
						$saveString = "ticketmaster".$this->eventDesc.implode(".",$filters);
						$ticketmaster_results = getCachedContent($saveString, ticketmasterAPI_findEvents($this->eventDesc, $filters));
					}
					if(strcasecmp($source, "eventful") == 0){
						$saveString = "eventful".$this->eventDesc.implode(".",$filters);
						$eventful_results = getCachedContent($saveString, eventfulAPI_findEvents($this->eventDesc, $filters));
					}
				}
				
				$this->items = array_merge($stubhub_results, $ticketmaster_results, $eventful_results);
				usort($this->items, 'sortgEventsByDate');
			} else { 
				$saveString = $this->eventDesc.implode(".",$filters);
				
				$stubhub_results = getCachedContent($saveString, stubhubAPI_findEvents($this->eventDesc, $filters));
				$ticketmaster_results = getCachedContent($saveString, ticketmasterAPI_findEvents($this->eventDesc, $filters));
				$eventful_results = getCachedContent($saveString, eventfulAPI_findEvents($this->eventDesc, $filters));
				$this->items = array_merge($stubhub_results, $ticketmaster_results, $eventful_results);			
				usort($this->items, 'sortgEventsByDate');
			}

			if($eventSearchRequestId == null){
				$eventSearchRequestId = $dbNeo->insertEventSearchRequest($searchJson, count($this->items));
				$dbNeo->createExternalEventsWithRel($this->items, $eventSearchRequestId);
	            echo "I created the external events in neo!";
			}
		}
		
		public function determineResult(){
			if($this->app->response->getStatus() == 200){
				$this->result = 'success';
			} else{
				$this->result = 'error';
			}
		}
		
		public function determineHref(){
			$serverProtocol = explode("/",$_SERVER['SERVER_PROTOCOL']);
			$serverProtocol = strtolower($serverProtocol[0])."://";
			$serverUrl = $serverProtocol.$_SERVER['SERVER_NAME'];
			$queryString = $_SERVER['QUERY_STRING'];
			
			if(!empty($queryString)){
				$this->href = $serverUrl.$this->app->request()->getPath()."?".$queryString;
			} else{
				$this->href = $serverUrl.$this->app->request()->getPath();
			}
		}

		public function determineMessage(){
			if($this->items != null){
				$this->message = count($this->items)." events found";
			} else{
				$this->message = 'No events found';
			}
		}

		public function determineNumFound(){
			if($this->items != null){
				$this->numFound = count($this->items);
			} else{
				$this->numFound = 0;
			}	
		}
		
		public function determinePagination(){
			if($this->numFound > 0 && $this->numFound > $this->limit){
				if($this->offset == null){
					$this->offset = 0;
					$this->first = array("href"=>$this->href);
					$this->previous = null;
				}
				$this->numFound = count($this->items);
			} else{
				$this->offset = 0;
				$this->limit = null;
				$this->first = array($this->href);
				$this->previous = null;
				$this->next = null;
				$this->last = null;
			}	
		}

		public function generateJson(){
			if($this->result == 'success'){
				$this->jsonWithoutChecksum = 
					json_encode(
						array(	
							"href"=>$this->href,
							"result"=>$this->result,
							"message"=>$this->message,
							"numFound"=>$this->numFound,
							"cursor"=>array(
								"offset"=>$this->offset,
								"limit"=>$this->limit,
								"first"=>$this->first,
								"previous"=>$this->previous,
								"next"=>$this->next,
								"last"=>$this->last),
							"items"=>$this->items
						), 
						JSON_FORCE_OBJECT | JSON_UNESCAPED_SLASHES
					);
				$checksum = $this->calculateChecksum($this->jsonWithoutChecksum);
			} else{
				$this->jsonWithoutChecksum = 
					json_encode(
						array(	
							"result"=>$this->result,
							"status"=>$this->app->response->getStatus(),
							"code"=>00001,
							"property"=>"unknown",
							"message"=>$this->message,
							"developerMessage"=>"Something went wrong, figure out how to fix it.",
							"moreInfo"=>"wiki.turtleboys.com"
						), 
						JSON_FORCE_OBJECT | JSON_UNESCAPED_SLASHES
					);
				$checksum = $this->calculateChecksum($this->jsonWithoutChecksum);
			}
			
			$this->jsonWithChecksum =
				json_encode(
					array_merge(
						json_decode($this->jsonWithoutChecksum, true), 
						array("checksum"=>$checksum)
					), 
					JSON_FORCE_OBJECT | JSON_UNESCAPED_SLASHES
				);
			
		}

		public function calculateChecksum($value){
			return hash('sha512', $value);
		}

		public function getJsonWithChecksum(){
			return $this->jsonWithChecksum;
		}

		public function getJsonWithoutChecksum(){
			return $this->jsonWithoutChecksum;
		}

		public function getResult(){
			return $this->result;
		}

		public function getMessage(){
			return $this->message;
		}
	}
?>