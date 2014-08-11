package com.sthaan.core

import com.sthaan.UnexpectedDataException


class UserController {

	def userService, actionValidationService
    
	def addUser() throws UnexpectedDataException{
		
		log.info params
		actionValidationService.validateActionParameters (["name", "phone", "addedByPhone"], params)
		
		def newUserPropertiesMap = [:]
		//user data
		newUserPropertiesMap.username = params.name
		newUserPropertiesMap.password = params.phone
		newUserPropertiesMap.addedByPhone = params.addedByPhone
		
		//profile data
		newUserPropertiesMap.profile = [:]
		newUserPropertiesMap.profile.name = params.name
		newUserPropertiesMap.profile.phone = params.phone
		newUserPropertiesMap.profile.pictureURL = params.pictureURL
		
		def saveStatus = userService.createUserAndUpdateContacts(newUserPropertiesMap)
		
		if (saveStatus.equals("existsAndAdded")){
			//added to contacts
		}else if (saveStatus.equals("savedAndAdded")){
			//ask to invite
		}
	}
	
}
