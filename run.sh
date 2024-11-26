#!/bin/bash

# 默认值
DEFAULT_VERSION="1.0"
DEFAULT_SERVICE_B_URI="http://170.106.189.13:10002"

# 显示使用方法
usage() {
   echo "Usage: $0 [version] [service_b_uri]"
   echo "Default version: $DEFAULT_VERSION"
   echo "Default URI: $DEFAULT_SERVICE_B_URI"
   echo "Example: $0 1.0 http://170.106.189.13:10002"
}

# 获取参数，使用默认值
VERSION=${1:-$DEFAULT_VERSION}
SERVICE_B_URI=${2:-$DEFAULT_SERVICE_B_URI}

# 容器配置
CONTAINER_NAME="service-a"
IMAGE_NAME="kkk2099/kkk"
TAG_NAME="service-a-1.0"
PORT="10001"

# 检查并移除已存在的容器
if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
   echo "Stopping and removing existing container..."
   docker stop $CONTAINER_NAME
   docker rm $CONTAINER_NAME
fi

# 运行新容器
echo "Starting $IMAGE_NAME:$TAG_NAME..."
echo "Version: $VERSION"
echo "Service B URI: $SERVICE_B_URI"

docker run -d \
   -p $PORT:$PORT \
   -e JVM_MEM=512M \
   -e APP_VERSION=$VERSION \
   -e SERVICE_B_URI=$SERVICE_B_URI \
   --name $CONTAINER_NAME \
   $IMAGE_NAME:$TAG_NAME

echo "Container started. Use 'docker logs $CONTAINER_NAME' to view logs."