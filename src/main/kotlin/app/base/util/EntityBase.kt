package app.base.util

import app.base.cutil.LangUtil.now
import io.ebean.annotation.DbDefault
import io.ebean.annotation.Identity
import io.ebean.annotation.IdentityType
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class EntityBase {

  @Identity(type = IdentityType.APPLICATION)
  @Id
  var id = 0L

  @DbDefault("now()")
  @Column(nullable = false)
  var updated = now()

  @DbDefault("now()")
  @Column(nullable = false)
  var created = now()

  @DbDefault("0")
  @Column(nullable = false)
  var version = 0L
}
