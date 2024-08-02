#!/bin/bash

# 设置变量
DOCKER_DIR="./docker"
JAR_NAME="springboot-0.0.1-SNAPSHOT.jar"
DOCKER_JAR_NAME="app.jar"
DOCKER_IMAGE_NAME="kkk2099/kkk:springboot-demo-1.0"

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
