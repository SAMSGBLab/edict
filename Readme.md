# Edict

## Project Description
Edict is a Javafx application that allows users to simulate iot systems modeled in NGSI-LD.Also it allows users to create their own iot systems and modify them.

## Getting Started
This repository contains the following directories:
* **edict--gui**: This directory contains the source code of the interface of the application.
* **edict--iotsimulator**: This directory contains the source code of the simulator that simulates the iot systems which is needed by the application.

### Installation Requirements
- Docker is required to run the application. You can download it from [here](https://www.docker.com/products/docker-desktop).
- Display server is required to run the application.

<B> For windows: </B>
- Xming you can download it from [here](https://sourceforge.net/projects/xming/).
OR
- VcXsrv you can download it from [here](https://sourceforge.net/projects/vcxsrv/).
  
<B>For linux: </B>
<B> TODO: add the display server for linux. </B>

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
```
docker run -it --rm -e  DISPLAY=host.docker.internal:0.0 -v <local/path>:<path/in/docker> <ImageName>
# example:
# docker run -it --rm -e  DISPLAY=host.docker.internal:0.0 -v /home/edict:/home edict
```

