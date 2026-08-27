@echo off
setlocal enabledelayedexpansion
title BT1 - LTWeb

set "JAVA_HOME=C:\Program Files\Java\jdk-20"
set "MAVEN_HOME=%USERPROFILE%\tools\apache-maven-3.9.9"
set "MYSQL_HOME=%USERPROFILE%\tools\mysql-8.0.40-winx64"
set "MYSQL_DATA=%USERPROFILE%\tools\mysql-data"
set "PROJECT_DIR=%~dp0"

echo ================================================
echo  BT1 - LTWeb : start app
echo ================================================

if not exist "%JAVA_HOME%\bin\java.exe" (
    echo [LOI] Khong tim thay JDK tai: %JAVA_HOME%
    goto :fail
)
if not exist "%MAVEN_HOME%\bin\mvn.cmd" (
    echo [LOI] Khong tim thay Maven tai: %MAVEN_HOME%
    goto :fail
)
if not exist "%MYSQL_HOME%\bin\mysqld.exe" (
    echo [LOI] Khong tim thay MySQL tai: %MYSQL_HOME%
    goto :fail
)
if not exist "%MYSQL_DATA%\mysql" (
    echo [0/3] Khoi tao data directory MySQL lan dau...
    "%MYSQL_HOME%\bin\mysqld.exe" --initialize-insecure --datadir="%MYSQL_DATA%" --basedir="%MYSQL_HOME%"
    if !errorlevel! neq 0 (
        echo [LOI] Khoi tao MySQL that bai.
        goto :fail
    )
)

echo [1/3] Kiem tra MySQL...
netstat -ano | findstr /R /C:":3306 .*LISTENING" >nul 2>&1
if !errorlevel! equ 0 (
    echo       MySQL da chay san tren cong 3306.
) else (
    echo       Dang khoi dong MySQL...
    start "MySQL 8.0.40 - BT1" /MIN "%MYSQL_HOME%\bin\mysqld.exe" --datadir="%MYSQL_DATA%" --basedir="%MYSQL_HOME%" --port=3306 --console

    set "READY="
    for /L %%i in (1,1,60) do (
        if not defined READY (
            "%MYSQL_HOME%\bin\mysqladmin.exe" -u root -h 127.0.0.1 -P 3306 ping >nul 2>&1
            if !errorlevel! equ 0 (
                set "READY=1"
                echo       MySQL san sang sau khoang %%i giay.
            ) else (
                timeout /t 1 /nobreak >nul
            )
        )
    )
    if not defined READY (
        echo [LOI] MySQL khong phan hoi sau 60 giay.
        goto :fail
    )
)

echo [2/3] Kiem tra database servletcrudmvc...
"%MYSQL_HOME%\bin\mysql.exe" -u root -h 127.0.0.1 -P 3306 -N -B -e "SELECT COUNT(*) FROM servletcrudmvc.category;" 2>nul
if !errorlevel! neq 0 (
    echo       Database chua co. Dang nap database.sql...
    "%MYSQL_HOME%\bin\mysql.exe" -u root -h 127.0.0.1 -P 3306 < "%PROJECT_DIR%database.sql"
    if !errorlevel! neq 0 (
        echo [LOI] Nap database.sql that bai.
        goto :fail
    )
    echo       Nap database thanh cong.
)

echo [3/3] Khoi dong Tomcat embedded ^(lan dau co the mat ~2 phut^)...
echo.
echo       App:            http://localhost:8080/BT1/
echo       Login Cookie:   http://localhost:8080/BT1/cookie/login
echo       Login Session:  http://localhost:8080/BT1/session/login
echo       Category CRUD:  http://localhost:8080/BT1/admin/category/list
echo       Tai khoan demo: trung / 123
echo.
echo       Nhan Ctrl+C de dung Tomcat. MySQL chay o cua so rieng.
echo ================================================
echo.

cd /d "%PROJECT_DIR%"
call "%MAVEN_HOME%\bin\mvn.cmd" tomcat7:run
goto :eof

:fail
echo.
echo Khoi dong that bai.
pause
exit /b 1
