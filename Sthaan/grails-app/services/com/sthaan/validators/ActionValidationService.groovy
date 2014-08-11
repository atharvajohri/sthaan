package com.sthaan.validators

import com.sthaan.UnexpectedDataException

class ActionValidationService {

    def validateActionParameters(requiredParameters, requestParameters) throws UnexpectedDataException {
		
		def actionParametersValid = true
		
		requiredParameters.each { reqdParameter ->
			
			if (!requestParameters[reqdParameter]){
				throw new UnexpectedDataException(requiredParameters)
			}
		}
		
		return actionParametersValid
    }
}
