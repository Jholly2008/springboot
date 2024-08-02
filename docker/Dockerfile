FROM mdsol/java21-jdk:latest
WORKDIR /data/apps
COPY . ./
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN chmod +x /data/apps/run.sh
ENTRYPOINT ["/data/apps/run.sh"]
