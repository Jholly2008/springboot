# springboot
springboot模板


## 构建
chmod +x build.sh
bash build.sh


## 启动
docker run -p 10001:10001 -e JVM_MEM=512M -e APP_VERSION=v1 --name service-a kkk2099/kkk:service-a-1.0