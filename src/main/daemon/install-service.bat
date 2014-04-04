@echo off

SETLOCAL

if NOT DEFINED RED5_HOME set RED5_HOME=%~dp0

echo Installing Red5 service
prunsrv //IS//REDFIVE --DisplayName="Red5" --Description="Red5 Media Server" --StartPath="%RED5_HOME%" --Type=interactive --LogLevel=DEBUG --LogPath="%RED5_HOME%\log" --LogPrefix=procrun.log --Startup=auto --StdOutput="%RED5_HOME%\log\stdout.log" --StdError="%RED5_HOME%\log\stderr.log" --Classpath="%RED5_HOME%\red5-server-bootstrap.jar;%RED5_HOME%\conf" --StartMode=jvm --StartClass=org.red5.server.Bootstrap --StopMode=jvm --StopClass=org.red5.server.Shutdown --StopParams=9999;red5user;changeme ++JvmOptions=-Djavax.net.ssl.keyStore="%RED5_HOME%/conf/keystore.jmx" ++JvmOptions=-Djavax.net.ssl.keyStorePassword=password ++JvmOptions=-Xverify:none ++JvmOptions=-XX:+TieredCompilation ++JvmOptions=-XX:+UseBiasedLocking ++JvmOptions=-XX:+UseStringCache ++JvmOptions=-XX:+UseParNewGC ++JvmOptions=-XX:InitialCodeCacheSize=8m ++JvmOptions=-XX:ReservedCodeCacheSize=32m ++JvmOptions=-Dorg.terracotta.quartz.skipUpdateCheck=true ++JvmOptions=-Dlogback.ContextSelector=org.red5.logging.LoggingContextSelector ++JvmOptions=-Dcatalina.useNaming=true ++JvmOptions=-Djava.security.debug=failure ++JvmOptions=-Djava.security.manager ++JvmOptions=-Djava.security.policy="%RED5_HOME%/conf/red5.policy" ++JvmOptions=-Dpython.home=lib

ENDLOCAL
