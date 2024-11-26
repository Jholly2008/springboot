#!/bin/bash

# 设置变量
DOCKER_DIR="./docker"
JAR_NAME="springboot-0.0.1-SNAPSHOT.jar"
DOCKER_JAR_NAME="app.jar"
DOCKER_IMAGE_NAME="kkk2099/kkk:service-a-1.0"

# 使用Maven打包，并跳过测试
mvn clean package -Dmaven.test.skip=true

# 检查Maven是否成功构建
if [ $? -ne 0 ]; then
  echo "Maven构建失败，脚本终止。"
  exit 1
fi

# 复制打包的JAR文件到Docker目录，并重命名为app.jar
cp "target/$JAR_NAME" "$DOCKER_DIR/$DOCKER_JAR_NAME"

# 检查复制是否成功
if [ $? -ne 0 ]; then
  echo "复制JAR文件失败，脚本终止。"
  exit 1
fi

# 进入Docker目录
cd "$DOCKER_DIR"

# 找到并停止和删除所有基于指定镜像的容器
CONTAINER_IDS=$(docker ps -a -q --filter ancestor="$DOCKER_IMAGE_NAME")

if [ -n "$CONTAINER_IDS" ]; then
  echo "找到容器，ID: $CONTAINER_IDS。"

  # 停止所有找到的容器
  echo "正在停止容器..."
  docker stop $CONTAINER_IDS

  # 检查停止容器是否成功
  if [ $? -ne 0 ]; then
    echo "停止容器失败，脚本终止。"
    exit 1
  fi

  # 删除所有找到的容器
  echo "正在删除容器..."
  docker rm $CONTAINER_IDS

  # 检查删除容器是否成功
  if [ $? -ne 0 ]; then
    echo "删除容器失败，脚本终止。"
    exit 1
  fi
else
  echo "未找到相关的容器。"
fi


# 找到Docker镜像的imageId
IMAGE_ID=$(docker images -q "$DOCKER_IMAGE_NAME")

# 删除旧的Docker镜像
if [ -n "$IMAGE_ID" ]; then
  docker rmi "$IMAGE_ID"
  # 检查镜像删除是否成功
  if [ $? -ne 0 ]; then
    echo "删除旧的Docker镜像失败，脚本终止。"
    exit 1
  fi
else
  echo "未找到旧的Docker镜像。"
fi

# 构建新的Docker镜像
docker build -t "$DOCKER_IMAGE_NAME" .

# 检查Docker构建是否成功
if [ $? -ne 0 ]; then
  echo "Docker镜像构建失败。"
  exit 1
fi

echo "Docker镜像构建成功。"
