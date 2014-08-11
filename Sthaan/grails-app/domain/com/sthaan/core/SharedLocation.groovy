package com.sthaan.core

class SharedLocation {

	User sharedBy
	User sharedTo
	
	Date dateCreated //shared on
	
	Location sharedByLocation
	Location sharedToLocation
		
    static constraints = {
    }
}
