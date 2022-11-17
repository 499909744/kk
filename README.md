## 启动jar包
java -jar kk.jar --NODE=10001 &

## 打包镜像：
1. 修改pom中docker.host为自己的docker服务器

2. 打包
   mvn clean package docker:build
   docker run -t -d -e NODE=2000 -p 18080:8080 web-platform/kk:1.0.0

## 访问
http://192.168.3.61:18080/health

## 配置mvn 的docker打包配置
```
1、搜索阿里云镜像服务、并注册私有镜像仓库或者自己搭建本地镜像仓库

2、配置maven
<server>
<id>docker-aliyun</id>
<username>laohu4521@163.com</username>
<password>xxxxx</password>
<configuration>
<email>laohu4521@163.com</email>
</configuration>
</server>

3、pom配置
<properties>
<docker.repository>registry.cn-shanghai.aliyuncs.com/dpw/</docker.repository>
<docker.registry.name>common-api</docker.registry.name>
</properties>

<build>
        <finalName>common-api</finalName>
        <plugins>
            <plugin>
                <groupId>com.spotify</groupId>
                <artifactId>docker-maven-plugin</artifactId>
                <version>0.4.13</version>
                <executions>
                    <execution>
                        <id>build-image</id>
                        <phase>package</phase>
                        <!--只需将插件的goal绑定在某个phase，添加后，执行mvn package 时，插件自动为我们构建Docker 镜像 -->
                        <goals>
                            <goal>build</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <!--基础镜像-->
                    <!--<baseImage>java</baseImage>-->
                    <baseImage>registry.cn-shanghai.aliyuncs.com/dpw/java8:1.0</baseImage>
                    <imageName>${docker.repository}${docker.registry.name}</imageName>
                    <imageTags>
                        <imageTag>
                            latest
                        </imageTag>
                    </imageTags>
                    <exposes>
                        <expose>4201</expose>
                        <expose>4211</expose>
                    </exposes>
                    <entryPoint>["java","-jar","/${project.build.finalName}.jar"]</entryPoint>
                    <!-- 指定Dockerfile所在的路径, Dockerfile方式才需要该配置 -->
                    <!--<dockerDirectory>${project.basedir}</dockerDirectory>-->
                    <resources>
                        <resource>
                            <targetPath>/</targetPath>
                            <directory>${project.build.directory}</directory>
                            <include>${project.build.finalName}.jar</include>
                        </resource>
                    </resources>
                    <!--必须和maven setting.xml中一致-->
                    <serverId>docker-aliyun</serverId>
                    <registryUrl>${docker.repository}</registryUrl>
                    <pushImage>true</pushImage>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>2.2.4.RELEASE</version>
                <!--加入下面两项配置-->
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <mainClass>com.yl.commonApi.CommonApiApplication</mainClass>
                    <includeSystemScope>true</includeSystemScope>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>
        </plugins>
    </build>

4、打包并推送到仓库
mvn clean package docker:build
mvn clean package docker:build -DimageVersion=0.0.4
mvn clean package docker:build -DpushImage
mvn clean package docker:build -DpushImageTag
mvn clean package docker:build -DdockerImageTags=tag1 -DdockerImageTags=tag2 -DpushImageTag

===========================================================================
============================================================================
1、错误
Deployment failed: repository element was not specified in the POM inside distributionManagement element or in -DaltDeploymentRepository=id::layout::url parameter
解决：
因为push镜像绑定到deploy中的，除非配置maven deploy的镜像地址，或者跳过maven部署
配置：
<distributionManagement>
<repository>
<id>release</id>
<url>http://192.168.3.110:8081/repository/yljt-release/</url>
</repository>
<snapshotRepository>
<id>snapshot</id>
<url>http://192.168.3.110:8081/repository/yljt-snapshot/</url>
</snapshotRepository>
</distributionManagement>

或者
<properties>
<maven.deploy.skip>true</maven.deploy.skip>
</properties>

```
