## 启动jar包
java -jar kk.jar --NODE=10001 &

## 打包镜像：
1. 修改pom中docker.host为自己的docker服务器

2. 打包并推送到仓库
   mvn clean package docker:build
   mvn clean package docker:build -DimageVersion=0.0.4
   mvn clean package docker:build -DpushImage
   mvn clean package docker:build -DpushImageTag
   mvn clean package docker:build -DdockerImageTags=tag1 -DdockerImageTags=tag2 -DpushImageTag

   docker run -t -d -e NODE=2000 -p 18080:8080 web-platform/kk:1.0.0

## 访问
http://192.168.3.61:18080/health