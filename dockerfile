 ## 基础镜像java
 FROM openjdk:8
 ## 作者是zsj
 MAINTAINER zsj
 ## 上传的jar包的名称。给jar包起个别名
 ADD reproducible-0.0.1-SNAPSHOT.jar spring_boot.jar
 ## 就是在容器中以多少端口号运行
 EXPOSE 9991
 ## 容器启动之后执行的命令，java -jar spring_boot.jar 即启动jar
 ENTRYPOINT ["java","-jar","spring_boot.jar"]
