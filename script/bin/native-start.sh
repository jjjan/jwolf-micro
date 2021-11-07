#! /bin/bash
#脚本说明： 执行sh restart.sh jwolf-service-user.jar进行启动，脚本根据jar包名找到进程号，kill再次重启，并输出日志
#注意1：-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8980 等为远程jvm debug
#注意2：JAVA_OPTS 为JVM参数，由于系统资源有限，设置得比较小，后期如果GC比较严重应适当调大
#注意3：启动成功后看到"Started XXXXApplication in 11.932... "ctrl + c退出


function log() {
      logfile=$1
      if [[ ! -f $logfile ]];then
        mkdir -p $logfile
      fi
      echo "日志目录: $logfile"
      tail -10f $logfile

}

if [ -z $1 ]; then
  echo '请指定要重启的微服务jar包，如sh restart.sh jwolf-service-user.jar'
else
  checkJarFile=$(find . -name $1)
  if [ -z $checkJarFile ]; then
    echo '当前目录未找到$1'
  else
    port=$(ps -ef | grep $1 | awk '{if($8=="java") print $2 }')
    kill -9 $port
    echo '开始重启>>>>'
    sleep 3
    if [[ $1 == *jwolf-service-user* ]]; then
      JAVA_OPTS='-server -Xmx256m -Xms256m -Xmn150m -Xss256k   -XX:CMSInitiatingOccupancyFraction=80  -XX:MetaspaceSize=80m -XX:MaxMetaspaceSize=100m -XX:+DisableExplicitGC  -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly  -Duser.timezone=GMT+8'
      nohup java -jar -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8980 $JAVA_OPTS $1 >/dev/null 2>&1 &
      log /var/log/jwolf/userLog/all.log
    fi
    if [[ $1 == *jwolf-service-msg* ]]; then
      JAVA_OPTS='-server -Xmx256m -Xms256m -Xmn150m -Xss256k   -XX:CMSInitiatingOccupancyFraction=80  -XX:MetaspaceSize=80m -XX:MaxMetaspaceSize=100m -XX:+DisableExplicitGC  -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly  -Duser.timezone=GMT+8'
      nohup java -jar -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8981 $JAVA_OPTS $1 >/dev/null 2>&1 &
      log /var/log/jwolf/msgLog/all.log
    fi
    if [[ $1 == *jwolf-service-travel* ]]; then
      JAVA_OPTS='-server -Xmx256m -Xms256m -Xmn150m -Xss256k   -XX:CMSInitiatingOccupancyFraction=80  -XX:MetaspaceSize=80m -XX:MaxMetaspaceSize=100m -XX:+DisableExplicitGC  -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly  -Duser.timezone=GMT+8'
      nohup java -jar -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8982 $JAVA_OPTS $1 >/dev/null 2>&1 &
      log /var/log/jwolf/travelLog/all.log
    fi
  fi
fi


