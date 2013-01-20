package twocents.commons

import com.typesafe.config.ConfigFactory

trait HasConfiguration {
	lazy val config = ConfigFactory.load.getConfig(getClass.getPackage.getName)

}