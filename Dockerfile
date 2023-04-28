FROM ubuntu:20.04

RUN apt-get update
RUN apt-get -qy dist-upgrade
RUN apt-get install openjdk-11-jdk -y 
RUN apt-get install maven -y
RUN apt-get install libgtk-3-0 -y
RUN apt-get install wget -y
RUN cd /opt; \
    mkdir JMT-v1.0; \
    cd JMT-v1.0; \
    wget --progress dot:mega -O jmt-singlejar-1.2.0.jar http://sourceforge.net/projects/jmt/files/jmt/JMT-1.2.0/JMT-singlejar-1.2.0.jar/download
WORKDIR /app

RUN mvn install:install-file -Dfile=/opt/JMT-v1.0/jmt-singlejar-1.2.0.jar \
        -DgroupId=jmt \
        -DartifactId=java-modelling-tools \
        -Dversion=1.0\
        -Dpackaging=jar \
        -DgeneratePom=true

RUN echo "export CLASSPATH=/usr/lib/jvm/java-11-openjdk-amd64/lib:/opt/JMT-v1.0/jmt-singlejar-1.2.0.jar" >> /home/$UNAME/.bashrc; 
# Copy the broker project files into the container
COPY edict--iotsimulator/pom.xml .
COPY edict--iotsimulator/src ./src
RUN mvn clean compile assembly:single

RUN mv target/broker-0.0.1-SNAPSHOT-jar-with-dependencies.jar iotsimulator.jar
# delete  pom.xml and src folder
RUN rm -rf pom.xml src target

# Copy the interface project files into the container
COPY edict--gui/pom.xml .
COPY edict--gui/src ./src
# RUN mvn compile
ENTRYPOINT [ "mvn","javafx:run" ]
