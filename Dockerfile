FROM amazoncorretto:21-alpine-jdk
LABEL maintainer="ahrooran17@gmail.com"

WORKDIR /apps
ENTRYPOINT ["java", "-jar", "*.jar"]


# running docker in windows -> connecting to postgresql and kafka on machine?
# https://stackoverflow.com/a/50394434/10582056
# https://medium.com/@kale.miller96/how-to-mount-your-current-working-directory-to-your-docker-container-in-windows-74e47fa104d7

# this will not work in windows: https://stackoverflow.com/a/71111055/10582056