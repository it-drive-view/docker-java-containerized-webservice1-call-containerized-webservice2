
1) clone the project 

2) go to folder microservice1
3) create jar with linux command : 
   mvn clean package 
   
4) go to folder microservice2
5) create jar with linux command : 
   mvn clean package 

# OPTION A : build and launch containers manually (without docker-compose)

1) go to folder microservice1
2) build image with linux command :
   docker build --no-cache -t java-ms1-image .
  
3) go to folder microservice2
4) build image with linux command :
   docker build --no-cache -t java-ms2-image .
   
5) we can check we have 2 images created by doing : 
   docker images | grep java

6) we create a network, so that the two future containers can see each other
   linux command : 
   docker network create java-network
   
7) before mappping local port 8081, we check there is no existing entry with this command : 
   netstat -tnlp | grep 8081 
   
8) run docker container 1 with command : 
   docker run --name microservice1 --network java-network -p 8081:8081 -d java-ms1-image
   
9) run docker container 2 with command : 
   docker run --name microservice2 --network java-network -d java-ms2-image
   
10) we can check we have 2 running containers by doing :
   docker ps | grep java 
    
### calling microservice2 from inside container1 (microservice1)
### (this is not nominal usage : this is just a test to illustrate how it works)

A) we go inside container1 and launch a bash :
   docker exec <microservice1-id> bash 
	   
B) from inside container1, we try to call microservice2
           curl -X GET http://microservice2:8082/api/v1/test
           
C) this is succesfull !
        
How it works : 
- container1 and container2 are on the same network (java-network) so container1 can contact container2
- container1 can call the domain name microservice2 because docker automatically created a DNS (microservice2 is the name of the container)
        
remark : 
- localhost can't contact : 
  curl -X GET http://microservice2:8082/api/v1/test
  because localhost and containers are not on the same network 

### calling microservice2 from localhost
### THIS IS NOMINAL USAGE

we will do : 
localhost ==> call container1 (with port mapping) :   http://localhost:8081/api/v1/call/microservice2
container1 ==> call container2 (by classic network) : http://microservice2:8082/api/v1/test
	
A) From localhost, we do :
   curl -X GET http://localhost:8081/api/v1/call/microservice2
	
B) this is succesfull !
	
How it works : 
- there is port mapping 8081:8081 so we can contact container1 from localhost using local port 8081
- container1 and container2 are on the same network (java-network) so container1 can contact container2
  (no port mapping between container1 and container2)
- container1 can call the domain name microservice2 because docker automatically created a DNS.

# OPTION B : build and launch containers using docker-compose

1) go to the root folder (where you can find docker-compose.yml file)
2) build the 2 images with linux command :
   docker-compose build --no-cache
   
3) we can check we have 2 images created by doing : 
   docker images | grep java

4) start the 2 containers with linux command :
   docker-compose up 
   
5) we can check we have 2 running containers by doing :
   docker ps | grep java 
   
Now, we can test : 

6) From localhost, we do :
   curl -X GET http://localhost:8081/api/v1/call/microservice2
	
7) this is succesfull !

   How it works : 
   - there is port mapping 8081:8081 so we can contact container1 from localhost
   - container1 and container2 are on the same network so container1 can contact container2 : 
     docker-compose automatically put the 2 containers on the same network, by creating a default network
   - container1 can call the domain name microservice2 because a DNS has been created while creating the network
   
   
   
   
   
   
   
   
   
   
   
  
   
   

