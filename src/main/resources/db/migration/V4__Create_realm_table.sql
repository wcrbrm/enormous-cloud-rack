
DROP TABLE IF EXISTS "realm";
CREATE TABLE IF NOT EXISTS "realm" (
  "realm_id"  VARCHAR PRIMARY KEY ,
  "status"    INT NOT NULL DEFAULT 1,
  "name"      VARCHAR NOT NULL,
  "logo"      VARCHAR NULL,
  "created_at" TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX ON "realm" ("status");

INSERT INTO "realm" ("realm_id", "name" ) VALUES ('testrealm', 'Realm for Testing');