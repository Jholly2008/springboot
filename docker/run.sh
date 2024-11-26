#!/bin/sh

echo PINPOINT_APPNAME:$PINPOINT_APPNAME
echo JVM_MEM:$JVM_MEM

if test -z "$PINPOINT_APPNAME"; then
    case "$JVM_MEM" in
        "512M")
            java -Xms400m -Xmx400m -Xmn128m -Xss512k -XX:SurvivorRatio=8 -XX:MaxDirectMemorySize=64m -XX:MetaspaceSize=50M -XX:MaxMetaspaceSize=50m -XX:+UnlockExperimentalVMOptions -XX:ConcGCThreads=1 -XX:ParallelGCThreads=1 -XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:ReservedCodeCacheSize=32m -XX:+UseCompressedClassPointers -XX:CompressedClassSpaceSize=24M -XX:-UseCounterDecay -XX:+AlwaysPreTouch -XX:+DisableExplicitGC -XX:+UnlockDiagnosticVMOptions -XX:GuaranteedSafepointInterval=0 -XX:+UseCountedLoopSafepoints -XX:LoopStripMiningIter=1000 -XX:+PrintCommandLineFlags -XX:+ExplicitGCInvokesConcurrent -XX:AutoBoxCacheMax=20000 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/usr/local/apps/jvm/ -XX:ErrorFile=/usr/local/apps/jvm/hs_err_%p.log -jar app.jar
            ;;
        "1G")
            java -Xms800m -Xmx800m -Xmn256m -Xss512k -XX:SurvivorRatio=8 -XX:MaxDirectMemorySize=128m -XX:MetaspaceSize=100M -XX:MaxMetaspaceSize=100m -XX:+UnlockExperimentalVMOptions -XX:ConcGCThreads=1 -XX:ParallelGCThreads=1 -XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:ReservedCodeCacheSize=64m -XX:+UseCompressedClassPointers -XX:CompressedClassSpaceSize=48M -XX:-UseCounterDecay -XX:+AlwaysPreTouch -XX:+DisableExplicitGC -XX:+UnlockDiagnosticVMOptions -XX:GuaranteedSafepointInterval=0 -XX:+UseCountedLoopSafepoints -XX:LoopStripMiningIter=1000 -XX:+PrintCommandLineFlags -XX:+ExplicitGCInvokesConcurrent -XX:AutoBoxCacheMax=20000 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/usr/local/apps/jvm/ -XX:ErrorFile=/usr/local/apps/jvm/hs_err_%p.log -jar app.jar
            ;;
        "2G")
            java -Xms1638m -Xmx1638m -Xmn512m -Xss512k -XX:SurvivorRatio=8 -XX:MaxDirectMemorySize=256m -XX:MetaspaceSize=200M -XX:MaxMetaspaceSize=200m -XX:+UnlockExperimentalVMOptions -XX:ConcGCThreads=1 -XX:ParallelGCThreads=2 -XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:ReservedCodeCacheSize=128m -XX:+UseCompressedClassPointers -XX:CompressedClassSpaceSize=48M -XX:-UseCounterDecay -XX:+AlwaysPreTouch -XX:+DisableExplicitGC -XX:+UnlockDiagnosticVMOptions -XX:GuaranteedSafepointInterval=0 -XX:+UseCountedLoopSafepoints -XX:LoopStripMiningIter=1000 -XX:+PrintCommandLineFlags -XX:+ExplicitGCInvokesConcurrent -XX:AutoBoxCacheMax=20000 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/usr/local/apps/jvm/ -XX:ErrorFile=/usr/local/apps/jvm/hs_err_%p.log -jar app.jar
            ;;
        "4G")
            java -Xms3276m -Xmx3276m -Xmn1024m -Xss512k -XX:SurvivorRatio=8 -XX:MaxDirectMemorySize=512m -XX:MetaspaceSize=300M -XX:MaxMetaspaceSize=300m -XX:+UnlockExperimentalVMOptions -XX:ConcGCThreads=2 -XX:ParallelGCThreads=4 -XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:ReservedCodeCacheSize=256m -XX:+UseCompressedClassPointers -XX:CompressedClassSpaceSize=48M -XX:-UseCounterDecay -XX:+AlwaysPreTouch -XX:+DisableExplicitGC -XX:+UnlockDiagnosticVMOptions -XX:GuaranteedSafepointInterval=0 -XX:+UseCountedLoopSafepoints -XX:LoopStripMiningIter=1000 -XX:+PrintCommandLineFlags -XX:+ExplicitGCInvokesConcurrent -XX:AutoBoxCacheMax=20000 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/usr/local/apps/jvm/ -XX:ErrorFile=/usr/local/apps/jvm/hs_err_%p.log -jar app.jar
            ;;
        "8G")
            java -Xms6144m -Xmx6144m -Xmn2048m -Xss512k -XX:SurvivorRatio=8 -XX:MaxDirectMemorySize=512m -XX:MetaspaceSize=400M -XX:MaxMetaspaceSize=400m -XX:+UnlockExperimentalVMOptions -XX:ConcGCThreads=2 -XX:ParallelGCThreads=4 -XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:ReservedCodeCacheSize=256m -XX:+UseCompressedClassPointers -XX:CompressedClassSpaceSize=48M -XX:-UseCounterDecay -XX:+AlwaysPreTouch -XX:+DisableExplicitGC -XX:+UnlockDiagnosticVMOptions -XX:GuaranteedSafepointInterval=0 -XX:+UseCountedLoopSafepoints -XX:LoopStripMiningIter=1000 -XX:+PrintCommandLineFlags -XX:+ExplicitGCInvokesConcurrent -XX:AutoBoxCacheMax=20000 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/usr/local/apps/jvm/ -XX:ErrorFile=/usr/local/apps/jvm/hs_err_%p.log -jar app.jar
            ;;
        *)
            echo "Invalid JVM_MEM value. Please set it to '1G', '2G', '4G', or '8G'."
            exit 1
            ;;
    esac
    echo "No pinpoint config..."
else
  export AGENT_PATH=/usr/local/apps/pinpoint-agent-3.0.0/pinpoint-bootstrap.jar
  export AGENT_NAME=$PINPOINT_APPNAME
  nameSize=${#HOSTNAME}
  if [ $nameSize -ge 5 ]; then
     export AGENT_ID_SUFFIX=${HOSTNAME:0-5}
  else
     export AGENT_ID_SUFFIX=$HOSTNAME
  fi
  if test -n "$AGENT_ID_SUFFIX"; then
     AGENT_ID=$PINPOINT_APPNAME-$AGENT_ID_SUFFIX
  else
     AGENT_ID=$PINPOINT_APPNAME
  fi
  export AGENT_OPTS="-javaagent:$AGENT_PATH -Dpinpoint.agentId=$AGENT_ID -Dpinpoint.applicationName=$AGENT_NAME"
        java $AGENT_OPTS -Xms6144m -Xmx6144m -Xmn2048m -Xss512k -XX:SurvivorRatio=8 -XX:MaxDirectMemorySize=512m -XX:MetaspaceSize=400M -XX:MaxMetaspaceSize=400m -XX:+UnlockExperimentalVMOptions -XX:ConcGCThreads=2 -XX:ParallelGCThreads=4 -XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:ReservedCodeCacheSize=256m -XX:+UseCompressedClassPointers -XX:CompressedClassSpaceSize=48M -XX:-UseCounterDecay -XX:+AlwaysPreTouch -XX:+DisableExplicitGC -XX:+UnlockDiagnosticVMOptions -XX:GuaranteedSafepointInterval=0 -XX:+UseCountedLoopSafepoints -XX:LoopStripMiningIter=1000 -XX:+PrintCommandLineFlags -XX:+ExplicitGCInvokesConcurrent -XX:AutoBoxCacheMax=20000 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/usr/local/apps/jvm/ -XX:ErrorFile=/usr/local/apps/jvm/hs_err_%p.log -jar app.jar
fi
