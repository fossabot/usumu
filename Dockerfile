FROM openjdk:8-jdk-alpine AS builder
VOLUME /tmp
RUN apk --no-cache add ca-certificates openssl &&  update-ca-certificates
RUN cd /tmp \
   && wget https://archive.apache.org/dist/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz \
   && wget https://archive.apache.org/dist/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz.sha1 \
   && echo -e "$(cat apache-maven-3.3.9-bin.tar.gz.sha1)  apache-maven-3.3.9-bin.tar.gz" | sha1sum -c - \
   && tar zxf apache-maven-3.3.9-bin.tar.gz \
   && rm -rf apache-maven-3.3.9-bin.tar.gz \
   && rm -rf *.sha1 \
   && mv ./apache-maven-3.3.9 /usr/share/maven \
   && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn
RUN mkdir /usr/src
COPY . /usr/src/
WORKDIR /usr/src
RUN mvn package

FROM openjdk:8-jdk-alpine AS runtime
VOLUME /tmp
RUN mkdir /opt/usumu
COPY --from=builder /usr/src/target/*.jar /opt/usumu
WORKDIR /opt/usumu

EXPOSE 8080

ENV USUMU_SECRET=""
ENV USUMU_INIT_VECTOR=""
ENV USUMU_S3_ACCESS_KEY_ID=""
ENV USUMU_S3_SECRET_ACCESS_KEY=""
ENV USUMU_S3_REGION=""
ENV USUMU_S3_BUCKET=""
ENV USUMU_S3_BUCKET_HOST=""

ENTRYPOINT ["/usr/bin/java", "-jar", "/opt/usumu/usumu-1.0-SNAPSHOT.jar"]
CMD []
