package com.sthaan.core

class User {

	transient springSecurityService

	String username
	String password
	boolean enabled = false
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	
	//foreign keys
	static hasOne = [profile: Profile, userStatus: UserStatus]
	static hasMany = [contacts: User, sharedLocations: SharedLocation]
	static mappedBy = [sharedLocations: 'sharedBy']
	
	static transients = ['springSecurityService']

	static constraints = {
		username blank: false, unique: true
		password blank: false
	}

	static mapping = {
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password)
	}
}
