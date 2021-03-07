FROM openjdk:12-alpine

LABEL version="1.0" description="Desafio Supply Java"

COPY ./target/app.jar /app/app.jar

WORKDIR /app

EXPOSE 8080

ENTRYPOINT exec java \
    -Dspring.datasource.driver-class-name=$DATABASE_DRIVER_CLASS \
    -Dspring.datasource.url=$DATABASE_URL \
    -Dspring.datasource.username=$DATABASE_USER \
    -Dspring.datasource.password=$DATABASE_PASSWORD \
    -Dspring.jpa.database-platform=$DATABASE_DIALECT \
    -Duser.country=BR \
    -Duser.language=pt \
    -Dfile.encoding=UTF8 \
    -server \
    -XX:+UseG1GC \
    -jar \
    app.jar
