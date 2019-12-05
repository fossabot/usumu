FROM openjdk:8-jdk-alpine
VOLUME /tmp
RUN mkdir /opt/usumu
COPY target/*.jar /opt/usumu/
WORKDIR /opt/usumu

EXPOSE 8080

ARG VERSION

ENV USUMU_SECRET=""
ENV USUMU_INIT_VECTOR=""
ENV USUMU_S3_ACCESS_KEY_ID=""
ENV USUMU_S3_SECRET_ACCESS_KEY=""
ENV USUMU_S3_REGION=""
ENV USUMU_S3_BUCKET=""
ENV USUMU_S3_BUCKET_HOST=""

RUN echo -e "#!/bin/sh\nexec /usr/bin/java -jar /opt/usumu/api-${VERSION}.jar $@" >/init.sh
RUN chmod +x /init.sh

ENTRYPOINT ["/init.sh"]
CMD []
