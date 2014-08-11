package com.sthaan.core

class PersistenceService {

    def showPersistenceErrors(object, errorMessage){
		log.error errorMessage ?: "[PERSISTENCE] Error while saving:"
		object.errors.each{
			println it
		}
	}
}
