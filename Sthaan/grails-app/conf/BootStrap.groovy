class BootStrap {

	def userService
	
    def init = { servletContext ->
		userService.createAdminUser("Admin ")
		userService.createUserStatuses()
    }
    def destroy = {
    }
}
