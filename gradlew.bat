@echo off
where gradle >nul 2>&1
if %ERRORLEVEL%==0 (
  gradle %*
) else (
  echo Gradle is not installed. Install Gradle 8.7 or run 'gradle wrapper' to generate a wrapper.
  exit /b 1
)
