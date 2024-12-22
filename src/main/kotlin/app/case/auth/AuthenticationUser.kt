package app.case.auth

import app.base.cutil.UserIdentity
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.impl.UserImpl

class AuthenticationUser(val identity: UserIdentity) : UserImpl(JsonObject(), JsonObject())
