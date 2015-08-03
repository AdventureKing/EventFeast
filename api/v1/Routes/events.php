<?php
	$app->get('/events/find/:eventDesc', function ($eventDesc) use ($app){
		
		$model = new findEventsModel($eventDesc, $app);
		$json_response = $model->getJsonWithChecksum();
		
		$app->contentType('application/json');
		echo $json_response;
	});	
?>