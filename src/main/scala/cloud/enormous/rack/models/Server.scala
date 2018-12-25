package cloud.enormous.rack.models

import com.byteslounge.slickrepo.meta.Entity
import sangria.macros.derive._

case class CreateServerInput(id: String, groupId: String, name: String, ip: String, tags: Option[List[String]] = Some(Nil))
case class UpdateServerInput(groupId: Option[String], name: Option[String], ip: Option[String], tags: Option[List[String]])

@GraphQLDescription(description = "Servers")
case class Server(

    override val id: Option[String],
    groupId: String,
    name: String,
    ip: String,
    tags: Option[List[String]]

) extends Entity[Server, String] {

  override def withId(id: String): Server = this.copy(
    id = Some(id), groupId = groupId, name = name, ip = ip, tags = tags
  )
  def updatedWith(input: UpdateServerInput): Server = {
    Server(id,
      input.groupId getOrElse this.groupId,
      input.name getOrElse this.name,
      input.ip getOrElse this.ip,
      input.tags orElse this.tags orElse Some(Nil)
    )
  }
}

/*
  "id"      VARCHAR PRIMARY KEY ,
  "realm_id" VARCHAR NOT NULL,
  "group_id" VARCHAR NOT NULL,
  "name"    VARCHAR NOT NULL,
  "status"  INT NOT NULL DEFAULT 1,
  "ip"      VARCHAR NOT NULL,  -- ip (+optional port)
  "tags"     TEXT[] not null default ARRAY[]::TEXT[],
  "mrtg"     VARCHAR NULL,
  "state"    VARCHAR NULL,
  "volumes"  TEXT[] not null default ARRAY[]::TEXT[],
  "auth_method"     VARCHAR NOT NULL DEFAULT 'password',
  "auth_user"       VARCHAR NOT NULL DEFAULT 'root',
  "auth_password"   VARCHAR NULL,
  "auth_privateKey" TEXT NULL
 */