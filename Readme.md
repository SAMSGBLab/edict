# Edict

## Project Description
Edict is a Javafx application that allows users to simulate iot systems modeled in NGSI-LD.Also it allows users to create their own iot systems and modify them.

## Getting Started
This repository contains the following directories:
* **edict--gui**: This directory contains the source code of the interface of the application.
* **edict--iotsimulator**: This directory contains the source code of the simulator that simulates the iot systems which is needed by the application.


## Installation
### <B>Docker</B> :
### Installation Requirements
- Docker is required to run the application. You can download it from [here](https://www.docker.com/products/docker-desktop).
- Display server is required to run the application.

<B> For windows: </B>
- Xming you can download it from [here](https://sourceforge.net/projects/xming/).
OR
- VcXsrv you can download it from [here](https://sourceforge.net/projects/vcxsrv/).
  
<B>For linux: </B>
- X11 server is already installed in linux.

### Installation Steps
- Clone the repository.
```
git clone https://github.com/SAMSGBLab/edict 
cd edict
```
- Build the docker image.
```
docker build -t <ImageName> .
```

### Running the application
- Run the docker image.
<B> For windows: </B>
```
docker run -it --rm -e  DISPLAY=host.docker.internal:0.0 -v <local/path>:<path/in/docker> <ImageName>
# example:
# docker run -it --rm -e  DISPLAY=host.docker.internal:0.0 -v /home/edict:/home edict
```
<B> For linux: </B>

```
# run the following commands in the terminal to allow the docker image to access the display server

export DISPLAY=:0.0
xhost +local:docker

docker run -it --rm -e  DISPLAY=$DISPLAY -v <local/path>:<path/in/docker> --net=host <ImageName>

# example:
# docker run -it --rm -e  DISPLAY=$DISPLAY -v /home/edict:/home --net=host edict

```

### <B>From source code</B> :
### Installation Requirements
- Java 11 or higher is required to run the application. You can download it from [here](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).
- Maven is required to build the application. You can download it from [here](https://maven.apache.org/download.cgi).
- JavaFX is required to run the application. You can download it from [here](https://gluonhq.com/products/javafx/).
- JMT is required to run the application. You can download it from [here](http://sourceforge.net/projects/jmt/files/jmt/JMT-1.2.0/JMT-singlejar-1.2.0.jar/download).

### Installation Steps
- Clone the repository.
```
git clone https://github.com/SAMSGBLab/edict 
cd edict
```
- Install JMT.
```
mvn install:install-file -Dfile=<path to jmt.jar> \
        -DgroupId=jmt \
        -DartifactId=java-modelling-tools \
        -Dversion=1.0\
        -Dpackaging=jar \
        -DgeneratePom=true
```
- Build the simulator.
```
cd edict--iotSimulator
mvn clean compile assembly:single
mv target/edict--iotSimulator-1.0-SNAPSHOT-jar-with-dependencies.jar ../edict--gui/src/main/iotsimulator.jar
```
- Build and run the application.
```
cd ../edict--gui
mvn clean javafx:run
```

## Usage

Running the application will open the following interface with modeling tab selected by default.

![home screen](/images/homeEmpty.png)

### Modeling

You can create a new device or application by clciking on the suitable button  then a modal will open to fill the details,then you can drag the created entities and resize them as you want.once you finish modeling you can click on save design button to save the design for later or click on the generate NGSI-LD button to generate the NGSI-LD representation of the model.

![modeling screen](/images/modeling.png)

### Simulation

You can simulate the iot system to generate useful data by clicking on the simulation tab then choose the data model location if you have a previous one or choose the one you just generated and choose the output location for the generated data then click on simulate button to start the simulation.


![simulation screen](/images/simulation.png)

### System Parameters
By clicking on the system parameters tab you can change the parameters of the system like the simulation duration and the Drop rates.

![system parameters screen](/images/systemParameters.png)



