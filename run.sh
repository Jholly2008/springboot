docker run -p 10001:10001 -e JVM_MEM=512M -e APP_VERSION=v1 -e SERVICE_B_URI=http://170.106.189.13:10002 --name service-a kkk2099/kkk:service-a-1.0