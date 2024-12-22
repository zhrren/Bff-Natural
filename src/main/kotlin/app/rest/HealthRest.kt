package app.rest

import app.base.cutil.D
import app.base.cutil.Rest
import com.google.inject.Inject
import io.ebeaninternal.server.util.Str

@D
class HealthRest @Inject constructor() : Rest {

  data class Status(val status: String)

  @D
  suspend fun status(): Status {
    return Status("UP")
  }
}

