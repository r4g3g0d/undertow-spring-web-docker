# Deploying Spring Based JAR Application to Docker

                



## Digital Ocean prerequisites
  1. Go to https://www.digitalocean.com/. Sign up.
  2. After Sign-In, in order to create a Droplet click Create -> Droplets. Default configurations of the droplet can be changed if it's required.
  3. Create Droplet. Wait.
  4. After the Droplet is created you will receive and email where IP Address,Username and Password is specified.
  5. Create a ssh connection by executing the following unix command :  
     ```ssh username@IPAddress```
  6. You will be asked to provide the password received via e-mail. After that you can change it.
  7.  After you are connected, create a new shell script file, using the following command  :
      vi install-docker.sh

      Paste the following code :
```
      sudo apt-get update  
sudo apt-get install \
    linux-image-extra-$(uname -r) \
    linux-image-extra-virtual
sudo apt-get install \
        apt-transport-https \
        ca-certificates \
        curl \
        software-properties-common
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo apt-key fingerprint 0EBFCD88
sudo add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
   $(lsb_release -cs) \
   stable"
sudo apt-get update
sudo apt-get install docker-ce
```

  8. Give the newly created script execution rights by executing the following unix command :
      chmod +x install-docker.sh

  9. Run the newly created script :    sudo ./install-docker.sh
  10. Verify the docker installation by running the hello world image :
      sudo docker run hello-world



## Local Machine Docker prerequisites
  1. Download Docker for desktop. Sign up.
  2. Go to the to be deployed project pom.xml path and build the project : ```mvn clean install ```
  3. Create Dockerfile. Populate it with the following code :  
        ```
        # Start with a base image containing Java runtime
        FROM openjdk:8-jdk-alpine
        
        
        # Add a volume pointing to /tmp
        VOLUME /tmp
        
        # Make port 8080 available to the world outside this container
        EXPOSE 8080
        
        # The application's jar file
        ARG JAR_FILE=target/uber-undertow-spring-web-1.0.0-SNAPSHOT.jar
        
        # Add the application's jar to the container
        ADD ${JAR_FILE} uber-undertow-spring-web.jar
        
        # Run the jar file
        ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/uber-undertow-spring-web.jar"] 
        ```
  4. In the same path run the following docker command in order to build the docker image : 
          ```docker build -t image_name .```
  5. In the same path run the newly created image with the following command(for testing purposes, locally) :  
          ```docker run -p 8080:8080 image_name:latest```
  6. In order to push the docker image to a docker repository, first we have to login :
          ```docker login```  
  7. After login in, tag the image and push it to docker repository with the following command :   
          ```docker tag image_name:latest docker_username/image_name:latest```  
          ```docker push docker_username/image_name:latest```

          
## Pull the newly created image and run it in previously created Digital Ocean Droplet
   1. Create a new ssh connection if the old connection to the Digital Ocean Droplet is not opened anymore.   
      ```ssh username@IPAddress```
   2. Pull the image previously pushed into docker repository and run it with the following commands :  
        ```docker pull docker_username/image_name:latest```  
        ```docker run -p 8080:8080 docker_username/image_name:latest```
   3. Test if the the deployment was successful by accessing the following address :  ```http://localhost:8080```  in any desired browser :   
        
   
###### Conclusion : Now we have our sample web application dockerized and pushed to a docker repository. Any system with a docker client installed can pull this docker image and run the web application instantly.                
      