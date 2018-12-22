package cloud.enormous.rack.models.db

import com.byteslounge.slickrepo.meta.Keyed
import com.byteslounge.slickrepo.repository.Repository
import cloud.enormous.rack.models.Server
import slick.ast.BaseTypedType
import slick.dbio.Effect
import slick.jdbc.JdbcProfile
import slick.lifted.MappedProjection
import slick.sql.SqlAction

class ServerRepository()(implicit override val driver: JdbcProfile)
  extends Repository[Server, String](driver) {

  import driver.api._

  val pkType = implicitly[BaseTypedType[String]]
  val tableQuery = TableQuery[Servers]
  type TableType = Servers

  class Servers(tag: slick.lifted.Tag) extends Table[Server](tag, "server") with Keyed[String] {

    def id: Rep[String] = column[String]("id", O.PrimaryKey)
    def groupId: Rep[String] = column[String]("group_id")
    def name: Rep[String] = column[String]("name")
    def ip: Rep[String] = column[String]("ip")

    def * = (id.?, groupId, name, ip) <> ((Server.apply _).tupled, Server.unapply)
  }

  def findById(id: String): DBIO[Seq[Server]] = {
    find(id = Some(id))
  }

  def find(
            id: Option[String] = None,
            groupId: Option[String] = None,
            name: Option[String] = None,
            ip: Option[String] = None
          ): DBIO[Seq[Server]] = {

    MaybeFilter(tableQuery)
      .filter(id)(v => d => d.id === v)
      .filter(groupId)(v => d => d.groupId === v)
      .filter(name)(v => d => d.name === v)
      .filter(ip)(v => d => d.ip === v)
      .query
      .sortBy(table => table.column[String]("id").asc)
      .result
  }
}
