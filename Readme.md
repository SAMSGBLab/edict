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

## Usage
after running the docker image, you will see the following interface:

![home screen](/images/homescreen.png)

### Model Data Generation
If you want to generate data from the model you created in this interface you can choose the data folder path in the generator part and click on generate data button.

### Model Data Simulation
If you want to simulate a model you can choose the path to the model folder in the simulator part and choose the path to the output folder and click on simulate button. 

