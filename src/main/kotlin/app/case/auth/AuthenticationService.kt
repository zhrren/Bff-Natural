package app.case.auth

import app.base.cutil.UserIdentity
import com.google.inject.Inject
import com.google.inject.Singleton
import io.vertx.ext.web.RoutingContext

/**
 *
 * Created by zhong on 2022/7/10
 */
@Singleton
class AuthenticationService @Inject constructor(
) {

  suspend fun handle(event: RoutingContext): AuthenticationUser? {
    return AuthenticationUser(UserIdentity(1, 1, "1", mapOf(), listOf(), listOf()))
  }
}
