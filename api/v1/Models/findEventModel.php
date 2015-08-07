<?php

	class findEventModel{
		private $app;

		private $eventExtId;
		private $eventExtSource;
		
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

	   	function __construct($eventId, $source, $app) {
	       $this->runEvent($eventId, $source, $app);
	   	}

	   	public function runEvent($eventId, $source, $app){
	   		$this->app = $app;
	   		$this->eventExtId = $eventId;
	   		$this->eventExtSource = $source;
	   		$this->generateItem();
			$this->determineHref();
	   		$this->determineResult();
	   		$this->determineMessage();
	   		$this->determineNumFound();
			$this->determinePagination();
	   		$this->generateJson();
	   	}

		public function generateItem(){
			$filters['userLat'] = $this->app->request()->params('userLat');
			$filters['userLng'] = $this->app->request()->params('userLng');
			
			if(isset($this->eventExtSource)){
				switch($this->eventExtSource){
					case "stubhub":
						$results = stubhubAPI_findEvent($this->eventExtId, $filters);
						break;
					case "ticketmaster":
						$results = ticketmasterAPI_findEvent($this->eventExtId, $filters);
						break;
					default:
						break;
				}
			}

			$this->items = $results;
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