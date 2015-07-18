<?php
	class CustomParseUser{
		private $firstName;
		private $lastName;
		private $username;
		private $email;
		private $password;
		private $birthday;
		private $apiKey;
	
	    /**
	     * Gets the value of firstName.
	     *
	     * @return mixed
	     */
	    public function getFirstName()
	    {
	        return $this->firstName;
	    }

	    /**
	     * Sets the value of firstName.
	     *
	     * @param mixed $firstName the first name
	     *
	     * @return self
	     */
	    public function setFirstName($firstName)
	    {
	        $this->firstName = $firstName;

	        return $this;
	    }

	    /**
	     * Gets the value of lastName.
	     *
	     * @return mixed
	     */
	    public function getLastName()
	    {
	        return $this->lastName;
	    }

	    /**
	     * Sets the value of lastName.
	     *
	     * @param mixed $lastName the last name
	     *
	     * @return self
	     */
	    public function setLastName($lastName)
	    {
	        $this->lastName = $lastName;

	        return $this;
	    }

	    /**
	     * Gets the value of username.
	     *
	     * @return mixed
	     */
	    public function getUsername()
	    {
	        return $this->username;
	    }

	    /**
	     * Sets the value of username.
	     *
	     * @param mixed $username the user name
	     *
	     * @return self
	     */
	    public function setusername($username)
	    {
	        $this->username = $username;

	        return $this;
	    }

	    /**
	     * Gets the value of email.
	     *
	     * @return mixed
	     */
	    public function getEmail()
	    {
	        return $this->email;
	    }

	    /**
	     * Sets the value of email.
	     *
	     * @param mixed $email the email
	     *
	     * @return self
	     */
	    public function setEmail($email)
	    {
	        $this->email = $email;

	        return $this;
	    }

	    /**
	     * Gets the value of password.
	     *
	     * @return mixed
	     */
	    public function getPassword()
	    {
	        return $this->password;
	    }

	    /**
	     * Sets the value of password.
	     *
	     * @param mixed $password the password
	     *
	     * @return self
	     */
	    public function setPassword($password)
	    {
	        $this->password = $password;

	        return $this;
	    }

	    /**
	     * Gets the value of birthday.
	     *
	     * @return mixed
	     */
	    public function getBirthday()
	    {
	        return $this->birthday;
	    }

	    /**
	     * Sets the value of birthday.
	     *
	     * @param mixed $birthday the birthday
	     *
	     * @return self
	     */
	    public function setBirthday($birthday)
	    {
	        $this->birthday = $birthday;

	        return $this;
	    }

	    /**
	     * Gets the value of apiKey.
	     *
	     * @return mixed
	     */
	    public function getApiKey()
	    {
	        return $this->apiKey;
	    }

	    /**
	     * Sets the value of apiKey.
	     *
	     * @param mixed $apiKey the api key
	     *
	     * @return self
	     */
	    public function setApiKey($apiKey)
	    {
	        $this->apiKey = $apiKey;

	        return $this;
	    }
}
?>