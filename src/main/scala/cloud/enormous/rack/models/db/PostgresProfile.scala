package cloud.enormous.rack.models.db
import com.github.tminglei.slickpg._

// importing api of this object allows you to have support for tags,
// stored in Postgres as native ARRAY[]TEXT

trait PostgresProfile extends ExPostgresProfile with PgArraySupport with PgSprayJsonSupport  {
  def pgjson = "jsonb"
  override val api = MyAPI
  object MyAPI extends API with ArrayImplicits with SprayJsonImplicits
}

object PostgresProfile extends PostgresProfile
