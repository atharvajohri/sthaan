package com.sthaan.core

class Profile {

	User user
	
	String name
	String phone
	String pictureURL
	Date dateCreated
	
    static constraints = {
		name nullable: true
		phone unique: true
		pictureURL nullable: true, url: true
    }
}
