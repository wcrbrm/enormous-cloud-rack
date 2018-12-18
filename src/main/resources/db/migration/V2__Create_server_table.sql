DROP TABLE IF EXISTS "server";
CREATE TABLE IF NOT EXISTS "server" (
  "id"      VARCHAR PRIMARY KEY ,
  "realm_id" VARCHAR NOT NULL DEFAULT 'testrealm',
  "group_id" VARCHAR NOT NULL DEFAULT '',
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
);

CREATE INDEX ON "server" USING gin ("tags");
CREATE INDEX ON "server" ("realm_id", "ip");
CREATE INDEX ON "server" ("realm_id", "name");
CREATE INDEX ON "server" ("realm_id", "group_id", "status");

INSERT INTO server
  (id, realm_id, group_id, name, ip, auth_password, tags)
VALUES 
 ('testserver1', 'testrealm', 'production', 'Production Server', '127.0.0.1:2200', 'root', ARRAY['tagone', 'tagcommon']),
 ('testserver2', 'testrealm', 'dev', 'Dev Server', '127.0.0.1:2201', 'root', ARRAY['tagtwo', 'tagcommon']);