<?php 
	class gEvent{
		/* Member Variables */
		private $internal_id = "";
		private $external_id = "";
		private $datasource = "";
		private $event_external_url = "";
		private $title = "";
		private $description = "";
		private $start_time = "";
		private $stop_time = "";
		private $venue_external_id = "";
		private $venue_external_url = "";
		private $venue_name = "";
		private $venue_display = "";
		private $venue_address = "";
		private $state_name = "";
		private $city_name = "";
		private $region_name = "";
		private $region_abbr = "";
		private $postal_code = "";
		private $country_name = "";
		private $all_day = "";
		private $latitude = "";
		private $longitude = "";
		private $performers = "";
		private $images = "";
	
	    /**
	     * Gets the value of internal_id.
	     *
	     * @return mixed
	     */
	    public function getInternal_id()
	    {
	        return $this->internal_id;
	    }

	    /**
	     * Sets the value of internal_id.
	     *
	     * @param mixed $internal_id the internal_id
	     *
	     * @return self
	     */
	    public function setInternal_id($internal_id)
	    {
	        $this->internal_id = $internal_id;

	        return $this;
	    }

	    /**
	     * Gets the value of external_id.
	     *
	     * @return mixed
	     */
	    public function getExternal_id()
	    {
	        return $this->external_id;
	    }

	    /**
	     * Sets the value of external_id.
	     *
	     * @param mixed $external_id the external_id
	     *
	     * @return self
	     */
	    public function setExternal_id($external_id)
	    {
	        $this->external_id = $external_id;

	        return $this;
	    }

	    /**
	     * Gets the value of datasource.
	     *
	     * @return mixed
	     */
	    public function getDatasource()
	    {
	        return $this->datasource;
	    }

	    /**
	     * Sets the value of datasource.
	     *
	     * @param mixed $datasource the datasource
	     *
	     * @return self
	     */
	    public function setDatasource($datasource)
	    {
	        $this->datasource = $datasource;

	        return $this;
	    }

	    /**
	     * Gets the value of event_external_url.
	     *
	     * @return mixed
	     */
	    public function getEvent_external_url()
	    {
	        return $this->event_external_url;
	    }

	    /**
	     * Sets the value of event_external_url.
	     *
	     * @param mixed $event_external_url the event_external_url
	     *
	     * @return self
	     */
	    public function setEvent_external_url($event_external_url)
	    {
	        $this->event_external_url = $event_external_url;

	        return $this;
	    }

	    /**
	     * Gets the value of title.
	     *
	     * @return mixed
	     */
	    public function getTitle()
	    {
	        return $this->title;
	    }

	    /**
	     * Sets the value of title.
	     *
	     * @param mixed $title the title
	     *
	     * @return self
	     */
	    public function setTitle($title)
	    {
	        $this->title = $title;

	        return $this;
	    }

	    /**
	     * Gets the value of description.
	     *
	     * @return mixed
	     */
	    public function getDescription()
	    {
	        return $this->description;
	    }

	    /**
	     * Sets the value of description.
	     *
	     * @param mixed $description the description
	     *
	     * @return self
	     */
	    public function setDescription($description)
	    {
	        $this->description = $description;

	        return $this;
	    }

	    /**
	     * Gets the value of start_time.
	     *
	     * @return mixed
	     */
	    public function getStart_time()
	    {
	        return $this->start_time;
	    }

	    /**
	     * Sets the value of start_time.
	     *
	     * @param mixed $start_time the start_time
	     *
	     * @return self
	     */
	    public function setStart_time($start_time)
	    {
	        $this->start_time = $start_time;

	        return $this;
	    }

	    /**
	     * Gets the value of stop_time.
	     *
	     * @return mixed
	     */
	    public function getStop_time()
	    {
	        return $this->stop_time;
	    }

	    /**
	     * Sets the value of stop_time.
	     *
	     * @param mixed $stop_time the stop_time
	     *
	     * @return self
	     */
	    public function setStop_time($stop_time)
	    {
	        $this->stop_time = $stop_time;

	        return $this;
	    }

	    /**
	     * Gets the value of venue_external_id.
	     *
	     * @return mixed
	     */
	    public function getVenue_external_id()
	    {
	        return $this->venue_external_id;
	    }

	    /**
	     * Sets the value of venue_external_id.
	     *
	     * @param mixed $venue_external_id the venue_external_id
	     *
	     * @return self
	     */
	    public function setVenue_external_id($venue_external_id)
	    {
	        $this->venue_external_id = $venue_external_id;

	        return $this;
	    }

	    /**
	     * Gets the value of venue_external_url.
	     *
	     * @return mixed
	     */
	    public function getVenue_external_url()
	    {
	        return $this->venue_external_url;
	    }

	    /**
	     * Sets the value of venue_external_url.
	     *
	     * @param mixed $venue_external_url the venue_external_url
	     *
	     * @return self
	     */
	    public function setVenue_external_url($venue_external_url)
	    {
	        $this->venue_external_url = $venue_external_url;

	        return $this;
	    }

	    /**
	     * Gets the value of venue_name.
	     *
	     * @return mixed
	     */
	    public function getVenue_name()
	    {
	        return $this->venue_name;
	    }

	    /**
	     * Sets the value of venue_name.
	     *
	     * @param mixed $venue_name the venue_name
	     *
	     * @return self
	     */
	    public function setVenue_name($venue_name)
	    {
	        $this->venue_name = $venue_name;

	        return $this;
	    }

	    /**
	     * Gets the value of venue_display.
	     *
	     * @return mixed
	     */
	    public function getVenue_display()
	    {
	        return $this->venue_display;
	    }

	    /**
	     * Sets the value of venue_display.
	     *
	     * @param mixed $venue_display the venue_display
	     *
	     * @return self
	     */
	    public function setVenue_display($venue_display)
	    {
	        $this->venue_display = $venue_display;

	        return $this;
	    }

	    /**
	     * Gets the value of venue_address.
	     *
	     * @return mixed
	     */
	    public function getVenue_address()
	    {
	        return $this->venue_address;
	    }

	    /**
	     * Sets the value of venue_address.
	     *
	     * @param mixed $venue_address the venue_address
	     *
	     * @return self
	     */
	    public function setVenue_address($venue_address)
	    {
	        $this->venue_address = $venue_address;

	        return $this;
	    }

		/**
	     * Gets the value of state_name.
	     *
	     * @return mixed
	     */
	    public function getState_name()
	    {
	        return $this->state_name;
	    }

	    /**
	     * Sets the value of state_name.
	     *
	     * @param mixed $state_name the state_name
	     *
	     * @return self
	     */
	    public function setState_name($state_name)
	    {
	        $this->state_name = $state_name;

	        return $this;
	    }

	    /**
	     * Gets the value of city_name.
	     *
	     * @return mixed
	     */
	    public function getCity_name()
	    {
	        return $this->city_name;
	    }

	    /**
	     * Sets the value of city_name.
	     *
	     * @param mixed $city_name the city_name
	     *
	     * @return self
	     */
	    public function setCity_name($city_name)
	    {
	        $this->city_name = $city_name;

	        return $this;
	    }

	    /**
	     * Gets the value of region_name.
	     *
	     * @return mixed
	     */
	    public function getRegion_name()
	    {
	        return $this->region_name;
	    }

	    /**
	     * Sets the value of region_name.
	     *
	     * @param mixed $region_name the region_name
	     *
	     * @return self
	     */
	    public function setRegion_name($region_name)
	    {
	        $this->region_name = $region_name;

	        return $this;
	    }

	    /**
	     * Gets the value of region_abbr.
	     *
	     * @return mixed
	     */
	    public function getRegion_abbr()
	    {
	        return $this->region_abbr;
	    }

	    /**
	     * Sets the value of region_abbr.
	     *
	     * @param mixed $region_abbr the region_abbr
	     *
	     * @return self
	     */
	    public function setRegion_abbr($region_abbr)
	    {
	        $this->region_abbr = $region_abbr;

	        return $this;
	    }

	    /**
	     * Gets the value of postal_code.
	     *
	     * @return mixed
	     */
	    public function getPostal_code()
	    {
	        return $this->postal_code;
	    }

	    /**
	     * Sets the value of postal_code.
	     *
	     * @param mixed $postal_code the postal_code
	     *
	     * @return self
	     */
	    public function setPostal_code($postal_code)
	    {
	        $this->postal_code = $postal_code;

	        return $this;
	    }

	    /**
	     * Gets the value of country_name.
	     *
	     * @return mixed
	     */
	    public function getCountry_name()
	    {
	        return $this->country_name;
	    }

	    /**
	     * Sets the value of country_name.
	     *
	     * @param mixed $country_name the country_name
	     *
	     * @return self
	     */
	    public function setCountry_name($country_name)
	    {
	        $this->country_name = $country_name;

	        return $this;
	    }

	    /**
	     * Gets the value of all_day.
	     *
	     * @return mixed
	     */
	    public function getAll_day()
	    {
	        return $this->all_day;
	    }

	    /**
	     * Sets the value of all_day.
	     *
	     * @param mixed $all_day the all_day
	     *
	     * @return self
	     */
	    public function setAll_day($all_day)
	    {
	        $this->all_day = $all_day;

	        return $this;
	    }

	    /**
	     * Gets the value of latitude.
	     *
	     * @return mixed
	     */
	    public function getLatitude()
	    {
	        return $this->latitude;
	    }

	    /**
	     * Sets the value of latitude.
	     *
	     * @param mixed $latitude the latitude
	     *
	     * @return self
	     */
	    public function setLatitude($latitude)
	    {
	        $this->latitude = $latitude;

	        return $this;
	    }

	    /**
	     * Gets the value of longitude.
	     *
	     * @return mixed
	     */
	    public function getLongitude()
	    {
	        return $this->longitude;
	    }

	    /**
	     * Sets the value of longitude.
	     *
	     * @param mixed $longitude the longitude
	     *
	     * @return self
	     */
	    public function setLongitude($longitude)
	    {
	        $this->longitude = $longitude;

	        return $this;
	    }

	    /**
	     * Gets the value of performers.
	     *
	     * @return mixed
	     */
	    public function getPerformers()
	    {
	        return $this->performers;
	    }

	    /**
	     * Sets the value of performers.
	     *
	     * @param mixed $performers the performers
	     *
	     * @return self
	     */
	    public function setPerformers($performers)
	    {
	        $this->performers = $performers;

	        return $this;
	    }

	    /**
	     * Gets the value of images.
	     *
	     * @return mixed
	     */
	    public function getImages()
	    {
	        return $this->images;
	    }

	    /**
	     * Sets the value of images.
	     *
	     * @param mixed $images the images
	     *
	     * @return self
	     */
	    public function setImages($images)
	    {
	        $this->images = $images;

	        return $this;
	    }
	}

	class gEventPerformer{
		private $performer_external_id;
		private $performer_external_url;
		private $performer_name;
		private $performer_short_bio;
	
	    /**
	     * Gets the value of performer_external_id.
	     *
	     * @return mixed
	     */
	    public function getPerformer_external_id()
	    {
	        return $this->performer_external_id;
	    }

	    /**
	     * Sets the value of performer_external_id.
	     *
	     * @param mixed $performer_external_id the performer_external_id
	     *
	     * @return self
	     */
	    public function setPerformer_external_id($performer_external_id)
	    {
	        $this->performer_external_id = $performer_external_id;

	        return $this;
	    }

	    /**
	     * Gets the value of performer_external_url.
	     *
	     * @return mixed
	     */
	    public function getPerformer_external_url()
	    {
	        return $this->performer_external_url;
	    }

	    /**
	     * Sets the value of performer_external_url.
	     *
	     * @param mixed $performer_external_url the performer_external_url
	     *
	     * @return self
	     */
	    public function setPerformer_external_url($performer_external_url)
	    {
	        $this->performer_external_url = $performer_external_url;

	        return $this;
	    }

	    /**
	     * Gets the value of performer_name.
	     *
	     * @return mixed
	     */
	    public function getPerformer_name()
	    {
	        return $this->performer_name;
	    }

	    /**
	     * Sets the value of performer_name.
	     *
	     * @param mixed $performer_name the performer_name
	     *
	     * @return self
	     */
	    public function setPerformer_name($performer_name)
	    {
	        $this->performer_name = $performer_name;

	        return $this;
	    }

	    /**
	     * Gets the value of performer_short_bio.
	     *
	     * @return mixed
	     */
	    public function getPerformer_short_bio()
	    {
	        return $this->performer_short_bio;
	    }

	    /**
	     * Sets the value of performer_short_bio.
	     *
	     * @param mixed $performer_short_bio the performer_short_bio
	     *
	     * @return self
	     */
	    public function setPerformer_short_bio($performer_short_bio)
	    {
	        $this->performer_short_bio = $performer_short_bio;

	        return $this;
	    }
	}

	class gEventImage{
		private $image_external_url;
		private $image_height;
		private $image_width;
	
	    /**
	     * Gets the value of image_external_url.
	     *
	     * @return mixed
	     */
	    public function getImage_external_url()
	    {
	        return $this->image_external_url;
	    }

	    /**
	     * Sets the value of image_external_url.
	     *
	     * @param mixed $image_external_url the image_external_url
	     *
	     * @return self
	     */
	    public function setImage_external_url($image_external_url)
	    {
	        $this->image_external_url = $image_external_url;

	        return $this;
	    }

	    /**
	     * Gets the value of image_height.
	     *
	     * @return mixed
	     */
	    public function getImage_height()
	    {
	        return $this->image_height;
	    }

	    /**
	     * Sets the value of image_height.
	     *
	     * @param mixed $image_height the image_height
	     *
	     * @return self
	     */
	    public function setImage_height($image_height)
	    {
	        $this->image_height = $image_height;

	        return $this;
	    }

	    /**
	     * Gets the value of image_width.
	     *
	     * @return mixed
	     */
	    public function getImage_width()
	    {
	        return $this->image_width;
	    }

	    /**
	     * Sets the value of image_width.
	     *
	     * @param mixed $image_width the image_width
	     *
	     * @return self
	     */
	    public function setImage_width($image_width)
	    {
	        $this->image_width = $image_width;

	        return $this;
    	}
	}

?>