FROM java:8

RUN mkdir -p /var/log/diff

RUN mkdir -p /manzoli/app/diff

WORKDIR /manzoli/app/diff

COPY . /manzoli/app/diff

EXPOSE 8080

CMD ["/usr/lib/jvm/java-8-openjdk-amd64/bin/java", "-jar", "target/diff.jar"]
