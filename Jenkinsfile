pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME'
    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t crud-app:latest .'
            }
        }

        stage('Docker Run') {
            steps {
                sh 'docker run -d -p 8080:8080 crud-app:latest'
            }
        }
    }
}
