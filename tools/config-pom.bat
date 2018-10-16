@echo off
setlocal Enabledelayedexpansion

if "%JAVA_HOME%"=="" (
	echo "ERROR: JAVA_HOME is not found, please check the environment configurations"
	goto end
)

REM Remeber the original directory, then it can be recovered at the end
set original_dir=%cd%

REM Get the project path, it is relative to the bat file
set bat_dir=%~dp0
cd /d %bat_dir%
cd ..
set project_dir=%cd%

REM Check if the template file of pom properties exist, if not then exit
set prop_template_file=%project_dir%\pom.properties.template
echo prop_template_file=%prop_template_file%
if not exist %prop_template_file% (
	echo "ERROR: property template file is not found, please check the files in project"
	goto end
)

REM Check if the pom.properties file exists, if not then copy from template
set property_file=%project_dir%\pom.properties
echo property_file=%property_file%
REM if not exist %property_file% (
	COPY "%prop_template_file%" "%property_file%" /Y
REM )

echo Try to get the ehapp.version...
for /f "tokens=1,2 delims==" %%a in (%property_file%) do (
	if "%%a"=="ehapp.version" (
		set ehapp_version=%%b
	)
)
echo Current ehapp.version is '!ehapp_version!', do you want to change?
set/p version_change=Input N to continue, others to change version: 
if /i not "%version_change%"=="N" (
	for /L %%x in (1, 1, 3) do (
		if "!version_new!"=="" (
			set/p version_new=Please input new version: 
		)
	)
	
	if "!version_new!"=="" (
		echo ERROR: no new version is set
		goto end
	)
	
	echo you have chosen to change the version '!ehapp_version!' to '!version_new!'
	goto replace_file
) else (
	goto pom_gen
)

:replace_file
set temp_file=%property_file%.temp
echo temp_file=%temp_file%
if exist %temp_file% (
	REM echo delete file %temp_file%
	del /q %temp_file%
)

for /f "tokens=1,2 delims==" %%a in (%property_file%) do (
	if "%%a"=="ehapp.version" ( 
		set line=ehapp.version=!version_new!
		echo !line! >> !temp_file!
	) else (
		set line=%%a=%%b
		REM echo key=%%a, value=%%b
		echo !line! >> !temp_file!
	)
)
del /q %property_file%
move /Y %temp_file% %property_file%

:pom_gen
REM Execute the command to generate pom and replace versions
echo Start to generate the pom by template, and replace all the version variables...
"%JAVA_HOME%/bin/java" -jar %project_dir%/tools/pom-sync-1.0.jar -R -e %project_dir%/pom.properties %project_dir%

REM Recover the directory to the start
cd /d %original_dir%

:end

pause