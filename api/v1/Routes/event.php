<?php
	$app->get('/event/external/:eventId', function ($eventId) use ($app){
		// check for required params
		verifyRequiredParams(array('source'));
		
		$source = $app->request->params('source');

		$model = new findEventModel($eventId, $source, $app);
		$json_response = $model->getJsonWithChecksum();
		
		$app->contentType('application/json');
		echo $json_response;
	});	
?>