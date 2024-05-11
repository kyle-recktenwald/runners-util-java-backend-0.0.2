pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/kyle-recktenwald/runners-util-java-backend-0.0.2'
            }
        }

        stage('Build') {
            steps {
                sh './gradlew clean build'
            }
        }

        stage('Test') {
            steps {
                sh './gradlew test'
            }
        }

        stage('Deploy') {
            steps {
                // Deploy your application to your environment
                // Add deployment steps here
            }
        }
    }

    post {
        always {
            // Clean up or perform any necessary actions
        }

        success {
            // Actions to perform if the pipeline succeeds
        }

        failure {
            // Actions to perform if the pipeline fails
        }

        unstable {
            // Actions to perform if the pipeline is unstable
        }

        changed {
            // Actions to perform if the pipeline status changes
        }
    }
}