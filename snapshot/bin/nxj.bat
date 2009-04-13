@echo off
if "%NXJ_HOME%" == ""  goto homeless

set THIRDPARTY_LIBS=%NXJ_HOME%\3rdparty\lib
set LINK_CLASSPATH=.;%THIRDPARTY_LIBS%\bcel-5.1.jar;%THIRDPARTY_LIBS%\commons-cli-1.0.jar;%NXJ_HOME%\lib\pctools.jar;%NXJ_HOME%\lib\pccomm.jar;%NXJ_HOME%\lib\jtools.jar;%THIRDPARTY_LIBS%\bluecove.jar

java -DCOMMAND_NAME=nxj "-Djava.library.path=%NXJ_HOME%\bin" "-Dnxj.home=%NXJ_HOME%" -classpath "%LINK_CLASSPATH%" lejos.pc.tools.NXJLinkAndUpload --writeorder LE --bootclasspath "%NXJ_HOME%\lib\classes.jar" --classpath "." %*

goto end

:homeless
echo NXJ_HOME not defined

:end