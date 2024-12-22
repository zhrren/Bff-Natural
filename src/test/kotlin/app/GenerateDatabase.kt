package app

object GenerateDatabase {
  /**
   * 初始化数据库
   */
  @JvmStatic
  fun main(args: Array<String>) {
    // 危险!!
//    runBlocking {
//      val automation = Automation(Vertx.vertx(), ".env.test")
//      automation.generateDatabase()
//      println("GenerateDatabase completed")
//    }
  }
}
