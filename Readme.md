# Edict

## Project Description
EDICT is a simulation tool for evaluating the performance of Edge interactions in IoT-enhanced environments.
The standard [NGSI-LD](https://fiware-datamodels.readthedocs.io/en/stable/ngsi-ld_howto/) (Next Generation Service Interfaces-Linked Data) protocol specification is used to represent systems deployed in IoT-enhanced environments. 
EDICT then generates a performance metrics dataset as a CSV file by relying on queueing network models.


## Getting Started
This repository contains the following directories:
* `edict--gui`:  contains the source code of the Graphical User Interface (GUI) of EDICT.
* `edict--iotsimulator`:  contains the source code of queueing network composer used to create and simulate queueing models. The queueing network composer is implemented on top of the [Java Modelling Tools](https://jmt.sourceforge.net/) (JMT) simulator.

## Installation Requirements
This artifact has been prepared for a host that supports X11 forwarding on Docker containers (e.g., by using [Xming](https://sourceforge.net/projects/xming/) or [VcXsrv](https://sourceforge.net/projects/vcxsrv/) on Windows, or X11 server for Linux). X11 forwarding is required by the JMT simulator, even when the graphical user interface is not used.
We provide a Docker container for running EDICT.  Users can also run EDICT from the source code on any host that supports [Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) or higher, [Maven](https://maven.apache.org/download.cgi), [JavaFx](https://gluonhq.com/products/javafx/) and [JMT](http://sourceforge.net/projects/jmt/files/jmt/JMT-1.2.0/JMT-singlejar-1.2.0.jar/download).
EDICT has been tested on GNU/Linux systems such as Debian or Ubuntu, and on Windows 10. EDICT has NOT been tested with MacOS.
## Installation Using Docker
To start using EDICT, first clone the current repository:
```
$ git clone https://github.com/SAMSGBLab/edict 
$ cd edict
```
Build the docker image by running the following command:
```
$ docker build -t <ImageName> .
```

To run the docker container, execute the following commands
**For windows:**
```
$ docker run -it --rm -e  DISPLAY=host.docker.internal:0.0 -v <local/path>:<path/in/docker> <ImageName>
# example:
# docker run -it --rm -e  DISPLAY=host.docker.internal:0.0 -v /home/edict:/home edict
```
<B> For linux: </B>

```
# run the following commands in the terminal to allow the docker image to access the display server

$ export DISPLAY=:0.0
$ xhost +local:docker

$ docker run -it --rm -e  DISPLAY=$DISPLAY -v <local/path>:<path/in/docker> --net=host <ImageName>

# example:
# docker run -it --rm -e  DISPLAY=$DISPLAY -v /home/edict:/home --net=host edict

```
 Now you are ready to start  [using EDICT](#using-edict).
## <B>Installation from Source Code</B> :
Start by cloning the current repository:
```
$ git clone https://github.com/SAMSGBLab/edict 
$ cd edict
```
Then install the Java Modelling Tools (JMT) simulator JAR file:
```
$ mvn install:install-file -Dfile=<path to jmt.jar> \
        -DgroupId=jmt \
        -DartifactId=java-modelling-tools \
        -Dversion=1.0\
        -Dpackaging=jar \
        -DgeneratePom=true
```
Finally, build the simulator:
```
$ cd edict--iotSimulator
$ mvn clean compile assembly:single
$ mv target/edict--iotSimulator-1.0-SNAPSHOT-jar-with-dependencies.jar ../edict--gui/src/main/iotsimulator.jar
```
You can now run EDICT:
```
$ cd ../edict--gui
$ mvn clean javafx:run
```

## Using EDICT

Running EDICT will open the following interface with the modeling tab selected by default:

![home screen](/images/homeEmpty.png)

You can use this drag & drop window to add devices and applications and edit their properties. 
Once you have added all your devices and applications, you can save the design to load later, or generate the NGSI-LD models of the setup you created.
![modeling screen](/images/modeling.png)

Alternatively, if you already have the NGSI-LD representation of your smart environment ready, you can use the *Simulate* tab specify the path to the folder where you have your JSON-LD files are saved. 

![simulation screen](/images/simulation.png)

You can use the *System Parameters* window to configure the parameters of the data exchange system.

![system parameters screen](/images/systemParameters.png)



