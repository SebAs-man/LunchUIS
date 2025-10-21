@echo off
REM set-env.bat - Load environment variables from .env

if not exist .env (
    echo ❌ Error: .env file not found!
    echo 📝 Please copy .env.example to .env and configure it:
    echo    copy .env.example .env
    exit /b 1
)

for /f "tokens=*" %%a in (.env) do (
    set %%a
)
echo ✅ Environment variables loaded from .env