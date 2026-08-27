@echo off
setlocal
title BT1 - stop app

set "MYSQL_HOME=%USERPROFILE%\tools\mysql-8.0.40-winx64"

echo Dang dung Tomcat (cong 8080)...
for /f "tokens=5" %%p in ('netstat -ano ^| findstr /R /C:":8080 .*LISTENING"') do (
    echo    kill PID %%p
    taskkill /F /PID %%p >nul 2>&1
)

echo Dang dung MySQL (cong 3306)...
"%MYSQL_HOME%\bin\mysqladmin.exe" -u root -h 127.0.0.1 -P 3306 shutdown >nul 2>&1
if %errorlevel% neq 0 (
    for /f "tokens=5" %%p in ('netstat -ano ^| findstr /R /C:":3306 .*LISTENING"') do (
        echo    kill PID %%p
        taskkill /F /PID %%p >nul 2>&1
    )
)

echo Da dung.
