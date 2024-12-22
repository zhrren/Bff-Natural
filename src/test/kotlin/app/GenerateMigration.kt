package app

import io.ebean.annotation.Platform
import io.ebean.dbmigration.DbMigration

object GenerateMigration {
  /**
   * Generate the next "DB schema DIFF" migration.
   */
  @JvmStatic
  fun main(args: Array<String>) {
    val dbMigration = DbMigration.create()
    dbMigration.setPlatform(Platform.POSTGRES)
    val m = dbMigration.generateMigration()
    println(m)
  }
}
