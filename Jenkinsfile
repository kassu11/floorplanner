pipeline {
  agent any

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
  }
}
