<?php 
	require_once($projectDir.'Class/gEventUtil.php');

	class gEvent implements JsonSerializable{
		/* Member Variables */
		private $internal_id = "";
		private $external_id = "";
		private $datasource = "";
		private $event_external_url = "";
		private $title = "";
		private $description = "";
		private $notes = "";
		private $timezone = "";
		private $timezone_abbr = "";
		private $start_time = "";
		private $end_time = "";
		private $start_date_month = array();
		private $start_date_day = array();
		private $start_date_year = array();
		private $start_date_time = array();
		private $end_date_month = array();
		private $end_date_day = array();
		private $end_date_year = array();
		private $end_date_time = array();
		private $venue_external_id = "";
		private $venue_external_url = "";
		private $venue_name = "";
		private $venue_display = "";
		private $venue_address = "";
		private $state_name = "";
		private $city_name = "";
		private $postal_code = "";
		private $country_name = "";
		private $all_day = false;
		private $price_range = "";
		private $is_free = null;
		private $major_genre = array();
		private $minor_genre = array();
		private $latitude = 0;
		private $longitude = 0;
		private $performers = array();
		private $images = array();
	
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
	     * Gets the value of notes.
	     *
	     * @return mixed
	     */
	    public function getNotes()
	    {
	        return $this->notes;
	    }

	    /**
	     * Sets the value of notes.
	     *
	     * @param mixed $notes the notes
	     *
	     * @return self
	     */
	    public function setNotes($notes)
	    {
	        $this->notes = $notes;

	        return $this;
	    }
		
		/**
	     * Gets the value of timezone.
	     *
	     * @return mixed
	     */
	    public function getTimezone()
	    {
	        return $this->timezone;
	    }

	    /**
	     * Sets the value of timezone.
	     *
	     * @param mixed $timezone the timezone
	     *
	     * @return self
	     */
	    public function setTimezone($timezone)
	    {
	        $this->timezone = $timezone;

	        return $this;
	    }
		
		/**
	     * Gets the value of timezone_abbr.
	     *
	     * @return mixed
	     */
	    public function getTimezone_abbr()
	    {
	        return $this->timezone_abbr;
	    }

	    /**
	     * Sets the value of timezone_abbr.
	     *
	     * @param mixed $timezone_abbr the timezone_abbr
	     *
	     * @return self
	     */
	    public function setTimezone_abbr($timezone_abbr)
	    {
	        $this->timezone_abbr = $timezone_abbr;

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
	     * Gets the value of end_time.
	     *
	     * @return mixed
	     */
	    public function getEnd_time()
	    {
	        return $this->end_time;
	    }

	    /**
	     * Sets the value of end_time.
	     *
	     * @param mixed $end_time the send_time
	     *
	     * @return self
	     */
	    public function setEnd_time($end_time)
	    {
	        $this->end_time = $end_time;

	        return $this;
	    }

	    /**
	     * Gets the value of start_date_month.
	     *
	     * @return mixed
	     */
	    public function getStart_date_month()
	    {
	        return $this->start_date_month;
	    }

	    /**
	     * Sets the value of start_date_month.
	     *
	     * @param mixed $start_date_month the start_date_month
	     *
	     * @return self
	     */
	    public function setStart_date_month($start_date_month)
	    {
	        $this->start_date_month = $start_date_month;

	        return $this;
	    }

	    /**
	     * Gets the value of start_date_day.
	     *
	     * @return mixed
	     */
	    public function getStart_date_day()
	    {
	        return $this->start_date_day;
	    }

	    /**
	     * Sets the value of start_date_day.
	     *
	     * @param mixed $start_date_day the start_date_day
	     *
	     * @return self
	     */
	    public function setStart_date_day($start_date_day)
	    {
	        $this->start_date_day = $start_date_day;

	        return $this;
	    }

	    /**
	     * Gets the value of start_date_year.
	     *
	     * @return mixed
	     */
	    public function getStart_date_year()
	    {
	        return $this->start_date_year;
	    }

	    /**
	     * Sets the value of start_date_year.
	     *
	     * @param mixed $start_date_year the start_date_year
	     *
	     * @return self
	     */
	    public function setStart_date_year($start_date_year)
	    {
	        $this->start_date_year = $start_date_year;

	        return $this;
	    }

	    /**
	     * Gets the value of start_date_time.
	     *
	     * @return mixed
	     */
	    public function getStart_date_time()
	    {
	        return $this->start_date_time;
	    }

	    /**
	     * Sets the value of start_date_time.
	     *
	     * @param mixed $start_date_time the start_date_time
	     *
	     * @return self
	     */
	    public function setStart_date_time($start_date_time)
	    {
	        $this->start_date_time = $start_date_time;

	        return $this;
	    }

	    /**
	     * Gets the value of end_date_month.
	     *
	     * @return mixed
	     */
	    public function getEnd_date_month()
	    {
	        return $this->end_date_month;
	    }

	    /**
	     * Sets the value of end_date_month.
	     *
	     * @param mixed $end_date_month the end_date_month
	     *
	     * @return self
	     */
	    public function setEnd_date_month($end_date_month)
	    {
	        $this->end_date_month = $end_date_month;

	        return $this;
	    }

	    /**
	     * Gets the value of end_date_day.
	     *
	     * @return mixed
	     */
	    public function getEnd_date_day()
	    {
	        return $this->end_date_day;
	    }

	    /**
	     * Sets the value of end_date_day.
	     *
	     * @param mixed $end_date_day the end_date_day
	     *
	     * @return self
	     */
	    public function setEnd_date_day($end_date_day)
	    {
	        $this->end_date_day = $end_date_day;

	        return $this;
	    }

	    /**
	     * Gets the value of end_date_year.
	     *
	     * @return mixed
	     */
	    public function getEnd_date_year()
	    {
	        return $this->end_date_year;
	    }

	    /**
	     * Sets the value of end_date_year.
	     *
	     * @param mixed $end_date_year the end_date_year
	     *
	     * @return self
	     */
	    public function setEnd_date_year($end_date_year)
	    {
	        $this->end_date_year = $end_date_year;

	        return $this;
	    }

	    /**
	     * Gets the value of end_date_time.
	     *
	     * @return mixed
	     */
	    public function getEnd_date_time()
	    {
	        return $this->end_date_time;
	    }

	    /**
	     * Sets the value of end_date_time.
	     *
	     * @param mixed $end_date_time the end_date_time
	     *
	     * @return self
	     */
	    public function setEnd_date_time($end_date_time)
	    {
	        $this->end_date_time = $end_date_time;

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
	     * Gets the value of price_range.
	     *
	     * @return mixed
	     */
	    public function getPrice_range()
	    {
	        return $this->price_range;
	    }

	    /**
	     * Sets the value of price_range.
	     *
	     * @param mixed $price_range the price_range
	     *
	     * @return self
	     */
	    public function setPrice_range($price_range)
	    {
	        $this->price_range = $price_range;

	        return $this;
	    }

	    /**
	     * Gets the value of is_free.
	     *
	     * @return mixed
	     */
	    public function getIs_free()
	    {
	        return $this->is_free;
	    }

	    /**
	     * Sets the value of is_free.
	     *
	     * @param mixed $is_free the is_free
	     *
	     * @return self
	     */
	    public function setIs_free($is_free)
	    {
	        $this->is_free = $is_free;

	        return $this;
	    }

		/**
	     * Gets the value of major_genre.
	     *
	     * @return mixed
	     */
	    public function getMajor_genre()
	    {
	        return $this->major_genre;
	    }

	    /**
	     * Sets the value of major_genre.
	     *
	     * @param mixed $major_genre the major_genre
	     *
	     * @return self
	     */
	    public function setMajor_genre($major_genre)
	    {
	        $this->major_genre = $major_genre;

	        return $this;
	    }

	    /**
	     * Gets the value of minor_genre.
	     *
	     * @return mixed
	     */
	    public function getMinor_genre()
	    {
	        return $this->minor_genre;
	    }

	    /**
	     * Sets the value of minor_genre.
	     *
	     * @param mixed $minor_genre the minor_genre
	     *
	     * @return self
	     */
	    public function setMinor_genre($minor_genre)
	    {
	        $this->minor_genre = $minor_genre;

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

	    // function called when encoded with json_encode
	    public function jsonSerialize()
	    {
	        return get_object_vars($this);
	    }
	}

	class gEventPerformer implements JsonSerializable{
		private $performer_external_id;
		private $performer_external_url;
		private $performer_external_image_url;
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
	     * Gets the value of performer_external_image_url.
	     *
	     * @return mixed
	     */
	    public function getPerformer_external_image_url()
	    {
	        return $this->performer_external_image_url;
	    }

	    /**
	     * Sets the value of performer_external_image_url.
	     *
	     * @param mixed $performer_external_image_url the performer_external_image_url
	     *
	     * @return self
	     */
	    public function setPerformer_external_image_url($performer_external_image_url)
	    {
	        $this->performer_external_image_url = $performer_external_image_url;

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

	    // function called when encoded with json_encode
	    public function jsonSerialize()
	    {
	        return get_object_vars($this);
	    }
	}

	class gEventImage implements JsonSerializable{
		private $image_external_url;
		private $image_category;
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
	     * Gets the value of image_category.
	     *
	     * @return mixed
	     */
	    public function getImage_category()
	    {
	        return $this->image_category;
	    }

	    /**
	     * Sets the value of image_category
	     *
	     * @param mixed $image_category the image_category
	     *
	     * @return self
	     */
	    public function setImage_category($image_category)
	    {
	        $this->image_category = $image_category;

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

    	// function called when encoded with json_encode
	    public function jsonSerialize()
	    {
	        return get_object_vars($this);
	    }

    }
?>
