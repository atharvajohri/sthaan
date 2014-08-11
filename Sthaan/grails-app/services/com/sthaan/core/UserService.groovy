package com.sthaan.core

class UserService {

	def persistenceService
	
	def createUserAndUpdateContacts(userPropertiesMap){
		def addedByUser = Profile.findByPhone(userPropertiesMap.addedByPhone).user ?: null
		return createUser(userPropertiesMap, addedByUser)
	}
	
	def createUser(newUserPropertiesMap, User addedByUser){
		
		def saveStatus = "unsaved"
		def existingUser = doesUserExist(newUserPropertiesMap.phone, getUserRole())
		if (!existingUser){
			def newUser = new User(newUserPropertiesMap)
			newUser.userStatus = UserStatus.findByCode("NIV") //create an uninvited user who is yet to join
			newUser.enabled = false
			
			def newUserProfile = new Profile(newUserPropertiesMap.profile)
			newUserProfile.user = newUser
			
			if (newUser.save(flush: true)){
				UserRole.create newUser, getUserRole(), true
				log.info "Successfully created new user \n[$newUser.profile.name\n$newUser.profile.phone]"

				saveStatus = "saved"
				if (addedByUser){
					log.info "New user was triggered by existing user... adding \n[$newUser.profile.name\n$newUser.profile.phone]\nto ${addedByUser.username}'s contacts"
					addedByUser.addToContacts newUser
					
					saveStatus = "savedAndAdded"
				}
			}else{
				persistenceService.showPersistenceErrors "ERROR! Could not save new user $newUser.username! Printing errors:\n", newUser
			}
		
		}else{
			log.info "Cannot create user $newUserPropertiesMap.username with number $newUserPropertiesMap.phone - Already exists!"
			saveStatus = "exists"
			if (addedByUser){
				log.info "New user was triggered by existing user... adding \n[$newUser.profile.name\n$newUser.profile.phone]\nto ${addedByUser.username}'s contacts"
				addedByUser.addToContacts existingUser
				saveStatus = "existsAndAdded"
			}
		}
		
		return saveStatus
	}
	
    def createAdminUser(userName) {
		createRoles()
		if (!doesUserExist(userName, getAdminRole())){
			def adminUser = new User(username: "admin", password: "iamanadmin", enabled: true, userStatus: UserStatus.findByCode("JOI") )
			def adminProfile = new Profile(phone: "+91 9008419521", name: userName, user: adminUser)
			
			if (adminUser.save(flush: true)){
				UserRole.create adminUser, getAdminRole(), true
				log.info "Admin user $userName created"
			}else{
				persistenceService.showPersistenceErrors "ERROR! Could not save admin user $userName! Printing errors:\n", adminUser
			}
		}else{
			log.info "'$userName' admin user already exists"
		}
    }
	
	def getUserRole(){
		Role.findByAuthority("ROLE_USER")
	}
	
	def getAdminRole(){
		Role.findByAuthority("ROLE_ADMIN")
	}
	
	def doesUserExist(String matchValue, UserRole userRole){
		def users = UserRole.findAllByRole(userRole).user
		
		def existingUser = null
		
		users.each { User user ->
			if (userRole.authority.equals("ROLE_ADMIN")){
				if (user && user.profile && user.profile.name.equals(matchValue)){
					existingUser = user
				}
			}else if (userRole.authority.equals("ROLE_USER")){
				if (user && user.profile && user.profile.phone.equals(matchValue)){
					existingUser = user
				}
			}
		}
		
		return existingUser
	}
	
	def createUserStatuses(){
		if (!UserStatus.list().size()){
			new UserStatus(code: "INV", name: "Invited").save()
			new UserStatus(code: "NIV", name: "Not Invited").save()
			new UserStatus(code: "JOI", name: "Joined").save()
		}else{
			log.info "User Statuses already exist!"
		}
	}
	
	def createRoles(){
		def adminRole = Role.findAllByAuthority("ROLE_ADMIN")
		if (!adminRole){
			adminRole = new Role(authority: 'ROLE_ADMIN').save(flush: true)
		}
		def userRole = Role.findAllByAuthority("ROLE_USER")
		if (!userRole){
			userRole = new Role(authority: 'ROLE_USER').save(flush: true)
		}
	}
}
