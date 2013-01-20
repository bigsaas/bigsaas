package twocents.commons

class MachineXSException(message : String, cause : Throwable = null) extends Exception(message, cause) {
  def message = getMessage
}

class InvalidInputException(message : String, cause : Throwable = null) extends MachineXSException(message, cause) 

/**
 * Thrown when the logged in user is not authorized. The message should be descriptive, in English and is meant for end-user
 * consumption.
 */
class NotAuthorizedException(message : String, cause : Throwable = null) extends MachineXSException(message, cause)

/**
 * Thrown when a specified id was not valid. The message should be descriptive, contain the , english and is meant for end-user
 * consumption.
 */
class InvalidIdException(val id : String, val `type` : String) extends InvalidInputException(`type`.capitalize + " with id " + id + " not found")
