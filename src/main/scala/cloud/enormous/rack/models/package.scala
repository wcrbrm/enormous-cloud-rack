package cloud.enormous.rack

package object models {
    
    case class Link(id: Int, url: String, description: String)

    case class Server(
	    _id: String,
	    accountId: String,
	    name: String,
	    ip: String,
	    comments: Option[String],
	    tags: Option[Set[String]],
	    state: Option[String],
	    mrtg: Option[String]
    )

    case class Account (
        accountId: String,
        status: String 
    )

    case class User(
        _id: String,
        accountId: String,
        login: Option[String],
        emails: Option[Set[String]],
        phones: Option[Set[String]]
    )

}