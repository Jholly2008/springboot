#!/bin/bash

# 容器配置
CONTAINER_NAME="service-a"
IMAGE_NAME="kkk2099/kkk:service-a-1.0"

# 检查容器是否存在
if [ "$(docker ps -aq -f name=$CONTAINER_NAME)" ]; then
   echo "Stopping container $CONTAINER_NAME..."
   docker stop $CONTAINER_NAME

   if [ $? -eq 0 ]; then
       echo "Removing container $CONTAINER_NAME..."
       docker rm $CONTAINER_NAME

       if [ $? -eq 0 ]; then
           echo "Container $CONTAINER_NAME stopped and removed successfully."
       else
           echo "Failed to remove container $CONTAINER_NAME."
           exit 1
       fi
   else
       echo "Failed to stop container $CONTAINER_NAME."
       exit 1
   fi
else
   echo "Container $CONTAINER_NAME does not exist."
fi

# 检查并删除镜像
if [ "$(docker images -q $IMAGE_NAME)" ]; then
   echo "Removing image $IMAGE_NAME..."
   docker rmi $IMAGE_NAME

   if [ $? -eq 0 ]; then
       echo "Image $IMAGE_NAME removed successfully."
   else
       echo "Failed to remove image $IMAGE_NAME."
       exit 1
   fi
else
   echo "Image $IMAGE_NAME does not exist."
fi

echo "Clean up completed successfully."