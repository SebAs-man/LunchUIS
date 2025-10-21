#!/bin/bash
# set-env.sh - Load environment variables from .env safely

if [ ! -f .env ]; then
    echo "❌ Error: .env file not found!"
    echo "📝 Please copy .env.example to .env and configure it:"
    echo "   cp .env.example .env"
    exit 1
fi

# Export all variables automatically when sourcing the file
set -a
# Use `.` or `source` to read the file directly, preserving quoting and ignoring comments
. .env
set +a

echo "✅ Environment variables loaded from .env"