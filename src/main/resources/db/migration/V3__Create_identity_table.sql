
DROP TABLE IF EXISTS "identity";
CREATE TABLE IF NOT EXISTS "identity" (
  "id"      VARCHAR PRIMARY KEY ,
  "realm_id" VARCHAR NOT NULL,
  "status"  INT NOT NULL DEFAULT 1,
  "login"   VARCHAR NOT NULL,
  "emails"     TEXT[] not null default '{}',
  "phones"     TEXT[] not null default '{}',
  "hash"       VARCHAR NOT NULL,
  "created_at" TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX ON "identity" ("realm_id", "login");
CREATE INDEX ON "identity"  USING GIN ("emails");
CREATE INDEX ON "identity"  USING GIN ("phones");
CREATE INDEX ON "identity" ("realm_id", "status");

INSERT INTO "identity"
    ("id", "realm_id", "status", "login", "emails", "phones", "hash")
VALUES
    ('user0', 'testrealm', 1, 'activeuser', ARRAY[]::TEXT[], ARRAY[]::TEXT[], '');