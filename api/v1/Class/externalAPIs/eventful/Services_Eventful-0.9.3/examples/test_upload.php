<?php
require dirname(__FILE__).'../Eventful.php';
error_reporting(E_ERROR | E_PARSE);

echo  "<title>Eventful PHP API Testing - image upload with Oauth </title>\n";
echo  "<p><center>Evenful PHP API testing - image upload with Oauth </center></p>";
// Enter your application key here. (See http://api.eventful.com/keys/)
// Authentication is required for some API methods.
   $conskey = 'Your Consumer Key Goes Here';
   $conssec = 'Your Consumer Key Goes Here';
   $oauth_token        = 'Your OAuth Token Goes Here';
   $oauth_token_secret = 'Your OAuth Secret Token Goes Here';
   $app_key = 'Your App Key Goes Here';

   $ev = new Services_Eventful($app_key,'');
   $ev->set_debug();
   $l = $ev->setup_Oauth($conskey,$conssec,$oauth_token,$oauth_token_secret);
   
   if ( PEAR::isError($l) ) {
       print("Can't setup oauth in: " . $l->getMessage() . "\n");
   }

   if (!empty($_FILES)) {
     // Set the image to upload and the caption here
     print("<br>API Results for uploading new file:\n<br>\n");
     $args = array(
        'caption'   => $_POST['caption'] ,
        'image_file'  => "{$_FILES['image_file']['tmp_name']}");
     $results = $ev->call('images/new', $args, 'json');
     print("<br>Results for uploading new file:\n<br>\n");
     print_r ($results);
   }
?>
<br><br>
<form action="" method="POST" enctype="multipart/form-data">
 <div>
   <label for="caption">Image Caption</label>
   <textarea type="text" name="caption" rows="1" cols="60"></textarea>
   <br />

   <label for="image">Photo</label>
   <input type="file" name="image_file" />
   <br />
   <input type="submit" value="Submit" />
 </div>
</form>
