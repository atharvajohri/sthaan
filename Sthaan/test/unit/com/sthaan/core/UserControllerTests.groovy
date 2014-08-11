
package com.sthaan.core

import grails.test.mixin.*

import org.junit.*

import com.sthaan.validators.ActionValidationService;

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(UserController)
@Mock([ActionValidationService])
class UserControllerTests {

    void testAddUser() {
		def params = [:]
		controller.actionValidationService = new ActionValidationService()
		when:
		controller.addUser()
		then:
		log.info response
    }
}
