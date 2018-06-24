@echo off

%~d0
cd %~p0

if NOT "%JAVA_HOME_7%" == "" (
    set JAVA_HOME="%JAVA_HOME_7%"
)

set MAVEN_OPTS=-Dhttps.protocols=TLSv1.1,TLSv1.2

call mvn clean
call mvn compile -Dmaven.test.skip=true
call mvn package -Dmaven.test.skip=true
call mvn source:jar -Dmaven.test.skip=true
call mvn javadoc:jar -Dmaven.test.skip=true -Dencoding=UTF-8 -Dcharset=UTF-8 -Ddecoding=UTF-8 -Dhttps.protocols=TLSv1.2
rem mvn javadoc:javadoc -Dencoding=UTF-8 -Dcharset=UTF-8 -Ddecoding=UTF-8 -Dmaven.test.skip=true

pause