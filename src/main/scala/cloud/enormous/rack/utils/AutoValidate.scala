package cloud.enormous.rack.utils
import java.util

import scala.collection.JavaConverters._

class AutoValidate extends Authentication {
  override def validateToken(token: String): util.Map[String, AnyRef] = Map[String, AnyRef]("token" -> token, "autoValidation" -> "true").asJava
}
