M1 - servers and users to be work
M2 - request / response of the state, obtained via SSH connection
M3 - authentication and realm restriction
M4 - client application to cover editing servers, and users, showing server status
M5 - docker container manipulation: view logs, restart, stop

===

M1: Implement Servers and Users via GraphQL
   - GraphQL filtration
	by realm "testrealm" + groupId,
        by realm "testrealm" and search keyword of tag
	by realm "testrealm" and search keyword of name
	by realm "testrealm" and search keyword of ip address
   - Servers CRUD
        addServer
        get server (via query), 
	update server information, 
        remove server
   - Users List 
   - Users CRUD
	add user
	get user,
	update user information
	remove user

===

M2:
   Status request
      - client could actually sent requests to each server id implicitly

   On Request 
      - server should connect to ssh  (1 via password, 2 via PEM)
          (client is reponsible to keep it, once we are out of request,
          ssh should be closed on timeout)
      - if not conencted, the connection should be kept
      - if connection is lost, it should re-connect

   What should be parsed / sent back to event bus then to client (for each serverId)
      - local server time
      - server uptime
      - load averages
      - number of threads
      - swap information
      - free disk space (/)
      - free disk space for every additional volume
      - server state from external command

   General information
      Docker version
      OS (name and version)
      # of cores (cat /proc/cpuinfo | grep "processor" | wc -l)

===

M3:
   Q: Requesting user token: is it a migration or what?
   - Users authentication
	- by login, positive and negative case
	- by email, positive and negative case
	- by phone, positive and negative case
        - disabled user should not return valid token

===
