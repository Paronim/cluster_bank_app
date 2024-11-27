#!/bin/bash
# restore_db.sh
echo "Restoring the database from dump..."
pg_restore -U postgres -d app_dbi /docker-entrypoint-initdb.d/app_dbi.dump