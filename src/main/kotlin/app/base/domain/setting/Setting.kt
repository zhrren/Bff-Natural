package app.base.domain.setting

import app.base.util.EntityBase
import io.ebean.annotation.DbDefault
import javax.persistence.Column
import javax.persistence.Table

@javax.persistence.Entity
@Table(name = "setting")
class Setting : EntityBase() {

  @DbDefault("")
  @Column
  var key = ""

  @DbDefault("")
  @Column
  var value = ""
}
