package cloud.enormous.rack.utils

import slick.basic.DatabaseConfig
import slick.dbio.{DBIO, DBIOAction, NoStream}
import slick.jdbc.JdbcProfile

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.language.implicitConversions

trait Profile {
  val profile: JdbcProfile
}


trait DbModule extends Profile{
  val db: JdbcProfile#Backend#Database

  implicit def executeOperation[T](databaseOperation: DBIO[T]): Future[T] = {
    db.run(databaseOperation)
  }
  implicit def executeAction[T](action: DBIOAction[T, NoStream, _]): T = {
    Await.result(db.run(action), Duration.Inf)
  }
}

trait PersistenceModule {
  implicit def executeOperation[T](databaseOperation: DBIO[T]): Future[T]
  implicit def executeAction[X](action: DBIOAction[X, NoStream, _]): X
}


class Persistence() extends PersistenceModule with DbModule {
  private val dbConfig : DatabaseConfig[JdbcProfile]  = DatabaseConfig.forConfig("database")
  override implicit val profile: JdbcProfile = dbConfig.profile
  override implicit val db: JdbcProfile#Backend#Database = dbConfig.db
}