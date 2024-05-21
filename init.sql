SELECT 'CREATE DATABASE runners_util_db' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'runners_util_db');

