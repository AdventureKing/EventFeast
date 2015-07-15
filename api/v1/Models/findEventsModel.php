<?php
	class findEventsModel{
		private $app;
		private $eventDesc;

		private $response;
		private $message;
		private $numFound;
		private $result;
		private $jsonWithoutChecksum;
		private $jsonWithChecksum;

		function __construct($eventDesc, $app) {
	       $this->run($eventDesc, $app);
	   	}

	   	public function run($eventDesc, $app){
	   		$this->app = $app;
	   		$this->eventDesc = $eventDesc;
	   		$this->generateResults();
	   		$this->determineResponse();
	   		$this->determineMessage();
	   		$this->determineNumFound();
	   		$this->generateJson();
	   	}

		public function generateResults(){
			$filters['city'] = $this->app->request()->params('city');
			$filters['state'] = $this->app->request()->params('state');
			$filters['date'] = $this->app->request()->params('date');
			$filters['desc'] = $this->app->request()->params('desc');
			$filters['sources'] = $this->app->request()->params('sources');

			if(!empty($filters['sources'])){
				$sourceArr = explode(",",$filters['sources']);
				foreach($sourceArr as $source){
					$stubhub_results = array();
					$ticketmaster_results = array();
					
					if(strcasecmp($source, "stubhub") == 0){ 
						$stubhub_results = stubhubAPI_findEvents($this->eventDesc, $filters);			
					}
					if(strcasecmp($source, "ticketMaster") == 0){
						$ticketmaster_results = ticketmasterAPI_findEvents($this->eventDesc, $filters);
					}

					$this->result = array_merge($stubhub_results, $ticketmaster_results);
				}
			} else { 
				$stubhub_results = stubhubAPI_findEvents($this->eventDesc, $filters);
				$ticketmaster_results = ticketmasterAPI_findEvents($this->eventDesc, $filters);
				$this->result = array_merge($stubhub_results, $ticketmaster_results);			
				usort($this->result, 'sortgEventsByDate');
			}
		}

		public function determineResponse(){
			if($this->app->response->getStatus() == 200){
				$this->response = 'success';
			} else{
				$this->response = 'error';
			}
		}

		public function determineMessage(){
			if($this->result != null){
				$this->message = count($this->result)." events found";
			} else{
				$this->message = 'No events found';
			}
		}

		public function determineNumFound(){
			if($this->result != null){
				$this->numFound = count($this->result);
			} else{
				$this->numFound = 0;
			}	
		}

		public function generateJson(){
			$this->jsonWithoutChecksum = 
				json_encode(
					array(	
						"response"=>$this->response,
						"message"=>$this->message,
						"numFound"=>$this->numFound,
						"result"=>$this->result
					), 
					JSON_FORCE_OBJECT | JSON_UNESCAPED_SLASHES
				);
			$checksum = $this->calculateChecksum($this->jsonWithoutChecksum);
			
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

		public function getResponse(){
			return $this->response;
		}

		public function getMessage(){
			return $this->message;
		}

		public function getResults(){
			return $this->result;
		}
	}
?>
