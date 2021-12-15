FROM java:8
EXPOSE 9402
ENV jarName jwolf-auth-1.0.0.jar
ENV JAVA_OPTS -server -Xmx400m -Xms400m -Xmn150m -Xss256k -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/oom-dump -XX:CMSFullGCsBeforeCompaction=2  -XX:CMSInitiatingOccupancyFraction=80  -XX:MetaspaceSize=100m -XX:MaxMetaspaceSize=200m -XX:+DisableExplicitGC  -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly  -Duser.timezone=GMT+8
ENV REMOTE_DEBUG -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=9502
ADD /target/$jarName $jarName
RUN echo "准备启动！"
ENTRYPOINT java $REMOTE_DEBUG $JAVA_OPTS -jar $jarName


