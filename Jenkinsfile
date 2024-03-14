pipeline {
  agent any

  environment {
    // Define Docker Hub credentials ID
    DOCKERHUB_CREDENTIALS_ID = 'nasimxx-docker'
    // Define Docker Hub repository name
    DOCKERHUB_REPO = 'nasimxx/floorplanner'
    // Define Docker image tag
    DOCKER_IMAGE_TAG = 'latest'
  }

  stages {
    stage('Checkout') {
      steps {
        git branch: 'main', credentialsId: 'Nasse_jenkins', url: 'https://github.com/kassu11/floorplanner.git'
      }
    }
    stage('Build') {
      steps {
        bat 'mvn clean install'
      }
    }
    stage('compile') {
      steps {
        bat 'mvn compile'
      }
    }
    stage('Test') {
      steps {
        bat 'mvn test'
      }
      post {
        success {
          // Publish JUnit test results
          junit '**/target/surefire-reports/TEST-*.xml'
          // Generate JaCoCo code coverage report
          jacoco(execPattern: '**/target/jacoco.exec')
        }
      }
    }
    stage('verify') {
      steps {
        bat 'mvn verify'
      }
    }
    stage('Build Docker Image') {  
      steps{                     
        sh 'docker-compose build'     
        echo 'Docker-compose-build Build Image Completed'                
      }           
    }
    stage('Login to Docker Hub') {          
      steps{                          
        sh 'echo $DOCKERHUB_CREDENTIALS_PSW | sudo docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'                     
        echo 'Login Completed'      
    }

    stage('Push Image to Docker Hub') {         
      steps{                            
        sh 'sudo docker push <dockerhubusername>/<dockerhubreponame>:$BUILD_NUMBER'           
        echo 'Push Image Completed'       
      }            
    }           
  }
    stage('Push Docker Image to Docker Hub') {
      steps {
          // Push Docker image to Docker Hub
        script {
          docker.withRegistry('https://index.docker.io/v1/', DOCKERHUB_CREDENTIALS_ID) {
            docker.image("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}").push()
          }
        }
      }
    }
  }
}
