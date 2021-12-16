FROM java:8
EXPOSE 8888
ENV jarName jwolf-manage-1.0.0.jar
ENV JAVA_OPTS -server -Xmx500m -Xms500m -Xmn150m -Xss256k -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/oom-dump -XX:CMSFullGCsBeforeCompaction=2  -XX:CMSInitiatingOccupancyFraction=80  -XX:MetaspaceSize=100m -XX:MaxMetaspaceSize=200m -XX:+DisableExplicitGC  -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly  -Duser.timezone=GMT+8
ENV REMOTE_DEBUG -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8988
ADD /target/$jarName $jarName
RUN echo "准备启动！"
ENTRYPOINT java $REMOTE_DEBUG $JAVA_OPTS -jar $jarName


