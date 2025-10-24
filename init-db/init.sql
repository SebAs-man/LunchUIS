-- This script runs automatically when PostgreSQL starts for the first time.

-- Create schemas for each microservice
CREATE SCHEMA IF NOT EXISTS identity;
CREATE SCHEMA IF NOT EXISTS combos;
CREATE SCHEMA IF NOT EXISTS orders;

-- Create UUID extension (required for the IDs)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";