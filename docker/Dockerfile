FROM azul/zulu-openjdk:11

ADD ${project.build.directory}/${project.build.finalName}.jar app.jar

EXPOSE 1015/tcp

CMD ["java", "-jar", "app.jar"]
