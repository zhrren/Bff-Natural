package app

import io.ebean.annotation.Platform
import io.ebean.dbmigration.DbMigration

object GenerateInitMigration {
  /**
   * Generate the next "DB schema DIFF" migration.
   */
  @JvmStatic
  fun main(args: Array<String>) {
    val dbMigration = DbMigration.create()
    dbMigration.setPlatform(Platform.POSTGRES)
    val m = dbMigration.generateInitMigration()
    println(m)
  }
}
