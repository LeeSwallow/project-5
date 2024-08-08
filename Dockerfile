# 베이스 이미지 설정 (예: OpenJDK 21)
FROM openjdk:21-jdk-slim

# 작업 디렉토리 설정
WORKDIR /home/min/WorkSpace/copies/project_TEAM5-2

# JAR 파일을 이미지에 복사
COPY build/libs/testAi-0.0.1-SNAPSHOT.jar testApp-0.0.1-SNAPSHOT.jar

# 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "testApp-0.0.1-SNAPSHOT.jar"]