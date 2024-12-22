package app.base.kernel

import app.base.cutil.Configuration

class Settings(private val config: Configuration) {

  val debug: Boolean = config.debug

  val api_id = config.get("api_id", "app")
  val api_mount_point = config.get("api_mount_point", "/api")
  val api_http_port = config.get("api_http_port", "12028").toInt()
  val api_apidoc_token = config.get("api_apidoc_token", "pLu3N2BIOMoVUJytCYiVR3X8q8ilhxp3")

  val db_username = config.get("db_username", "natural")
  val db_password = config.get("db_password")
  val db_host = config.get("db_host")
  val db_port = config.get("db_port", "5432").toInt()
  val db_name = config.get("db_name", "natural")
}
