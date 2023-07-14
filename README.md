# Distributed-Sorting-Using-Kafka


## Requirements

* x86-64
* JDK 17
* Linux
* Docker
* Kubernetes


## Setup

Before one is able to run this program, one has to first build the necessary
infrastructure. A shell script named "up" has been provided for this.
It has two modes: local and distributed. 

### Local
Local will
create Kafka, Zookeeper, Kafdrop and a Postgresql DB for us by means of
invoking two docker-compose files (one for Kafka and one for DB).

```
λ sh up.sh local 
```

### Distributed
Distributed will create our Kubernetes cluster consisting of 
1 admin, 1 merger and 10 sorter deployments all derived from our containerized codebase.

```
λ sh up.sh distributed 
```


## Running

### Local
Local runs are achieved by means of the maven plugin spring-boot.
```
λ mvn spring-boot:run
```


### Distributed
After resources have been allocated with our startup script, one
may proceed to connect to a given admin pod by issuing the following command

```
λ kubectl port-forward {POD} 8080
```


## Destroying resources
The shell script "down.sh" frees up allocated resources. 
It also comes with two modes: local and distributed. 
The local flag will merely run a docker-compose down command
whereas the distributed flag will destroy Kubernetes resources.

## HTTP Endpoints

The admin deployment has access to our HTTP endpoints via the AdminController

POST http://localhost:8080/kafka/createUser

GET http://localhost:8080/kafka/validateUser

POST http://localhost:8080/kafka/startQuicksort

## Kafdrop

Kafdrop is a popular Web UI for Kafka that allows you to monitor a cluster, view topics and consumer groups

Accessible from http://localhost:9000 in your browser
