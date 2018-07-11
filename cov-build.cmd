@echo off

SET COVERITY=D:\Coverity
SET COVERITY_BIN=%COVERITY%\bin
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_172
set MAVEN=C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2018.1.4\plugins\maven\lib\maven3
set PATH=%JAVA_HOME%\bin;%MAVEN%\bin;%PATH%

%COVERITY_BIN%\cov-build.exe --dir cov-int mvn clean compile
