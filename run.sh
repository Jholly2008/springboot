#!/bin/bash

# 显示使用方法
usage() {
   echo "Usage: $0 <version> <service_b_uri>"
   echo "Example: $0 1.0 http://170.106.189.13:10002"
   exit 1
}

# 检查参数个数
if [ $# -ne 2 ]; then
   usage
fi

# 获取参数
VERSION=$1
SERVICE_B_URI=$2

# 容器配置
CONTAINER_NAME="service-a"
IMAGE_NAME="kkk2099/kkk"
SERVICE_NAME="service-a"
PORT="10001"

# 检查并移除已存在的容器
if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
   echo "Stopping and removing existing container..."
   docker stop $CONTAINER_NAME
   docker rm $CONTAINER_NAME
fi

# 运行新容器
echo "Starting $SERVICE_NAME..."
echo "Version: $VERSION"
echo "Service B URI: $SERVICE_B_URI"

docker run -d \
   -p $PORT:$PORT \
   -e JVM_MEM=512M \
   -e APP_VERSION=$VERSION \
   -e SERVICE_B_URI=$SERVICE_B_URI \
   --name $CONTAINER_NAME \
   $IMAGE_NAME:$SERVICE_NAME-$VERSION

echo "Container started. Use 'docker logs $CONTAINER_NAME' to view logs."