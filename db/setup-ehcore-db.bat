@echo off

@REM step 1: config the host,user,password bellow, it's used to access the MySQL database;
@REM setp 2: execute the command 'setup-ehcore-db.bat'
set DB_HOST=127.0.0.1
set DB_USER=root
set DB_PASSWORD=
set DEVELOPER=1

set EHCORE_DATABASE_SCHEMA_FILE=ehcore-database.sql
@REM set EHCORE_PLATFORM_SCHEMA_FILE=ehcore-platform-schema.sql
set EHCORE_SERVER_SCHEMA_FILE=ehcore-server-schema.sql
set EHCORE_SYSTEM_INIT_SQL_FILE=ehcore-system-init.sql
set EHCORE_INIT_SQL_FILE=ehcore-init-data.sql
set EHCORE_DEVELOPER_INIT_SQL_FILE=ehcore-developer-init.sql
set DEFAULT_MY_INI=my.ini
set DB_NAME=ehcore

@REM ========================================================================================
@REM Keep the codes below readonly
@REM ========================================================================================
if "%DB_HOST%"=="" (
	echo [ERROR] %date:~0,10% %time:~0,22% The db host should be not empty
	goto end
) 
if "%DB_USER%"=="" (
	echo [ERROR] %date:~0,10% %time:~0,22% The db user should be not empty
	goto end
) 
if "%DB_PASSWORD%"=="" (
	echo [ERROR] %date:~0,10% %time:~0,22% The db password should be not empty
	goto end
) 

if not exist %EHCORE_DATABASE_SCHEMA_FILE% (
	echo [ERROR] %date:~0,10% %time:~0,22% Sql file '%EHCORE_DATABASE_SCHEMA_FILE%' is not exists
	goto end
) 
if not exist %EHCORE_SERVER_SCHEMA_FILE% (
	echo [ERROR] %date:~0,10% %time:~0,22% Sql file '%EHCORE_SERVER_SCHEMA_FILE%' is not exists
	goto end
)  
 
if not exist %EHCORE_SYSTEM_INIT_SQL_FILE% (
	echo [ERROR] %date:~0,10% %time:~0,22% Sql file '%EHCORE_SYSTEM_INIT_SQL_FILE%' is not exists
	goto end
)
if not exist %EHCORE_INIT_SQL_FILE% (
	echo [ERROR] %date:~0,10% %time:~0,22% Sql file '%EHCORE_INIT_SQL_FILE%' is not exists
	goto end
)
if %DEVELOPER% == 1 (
	if not exist %EHCORE_DEVELOPER_INIT_SQL_FILE% (
		echo [ERROR] %date:~0,10% %time:~0,22% Sql file '%EHCORE_DEVELOPER_INIT_SQL_FILE%' is not exists
		goto end
	)
)

if exist %DEFAULT_MY_INI% (
	DEL /F /Q %DEFAULT_MY_INI%
) 
echo [client] >> %DEFAULT_MY_INI%
echo default-character-set = utf8mb4 >> %DEFAULT_MY_INI%
echo host='%DB_HOST%' >> %DEFAULT_MY_INI%
echo user='%DB_USER%' >> %DEFAULT_MY_INI%
echo password='%DB_PASSWORD%' >> %DEFAULT_MY_INI%

if not exist %DEFAULT_MY_INI% (
	echo [ERROR] %date:~0,10% %time:~0,22% The MySQL config file '%DEFAULT_MY_INI%' is not exists
	goto end
) 
echo [INFO] %date:~0,10% %time:~0,22% Please make sure that the user and password is ready in '%DEFAULT_MY_INI%' before continue...

echo [INFO] %date:~0,10% %time:~0,22% Start to execute sql '%EHCORE_DATABASE_SCHEMA_FILE%' ...
mysql --defaults-file=%DEFAULT_MY_INI% %DB_NAME% < %EHCORE_DATABASE_SCHEMA_FILE%

echo [INFO] %date:~0,10% %time:~0,22% Start to execute sql '%EHCORE_SERVER_SCHEMA_FILE%' ...
mysql --defaults-file=%DEFAULT_MY_INI% %DB_NAME% < %EHCORE_SERVER_SCHEMA_FILE%

echo [INFO] %date:~0,10% %time:~0,22% Start to execute sql '%EHCORE_SYSTEM_INIT_SQL_FILE%' ...
mysql --defaults-file=%DEFAULT_MY_INI% %DB_NAME% < %EHCORE_SYSTEM_INIT_SQL_FILE%

echo [INFO] %date:~0,10% %time:~0,22% Start to execute sql '%EHCORE_INIT_SQL_FILE%' ...
mysql --defaults-file=%DEFAULT_MY_INI% %DB_NAME% < %EHCORE_INIT_SQL_FILE%



if %DEVELOPER% == 1 (
	echo [INFO] %date:~0,10% %time:~0,22% Start to execute sql '%EHCORE_DEVELOPER_INIT_SQL_FILE%' ...
	mysql --defaults-file=%DEFAULT_MY_INI%  %DB_NAME% < %EHCORE_DEVELOPER_INIT_SQL_FILE%
)


:end

if exist %DEFAULT_MY_INI% (
	DEL /F /Q %DEFAULT_MY_INI%
) 

pause

