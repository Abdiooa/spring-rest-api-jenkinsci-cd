pipeline {
    agent {
        node {
            label 'docker-agent-python'
        }
    }
    triggers {
        pollSCM 'H/2 * * * *'
    }
    tools {
        maven 'jenkins-maven'
        jdk 'java-17'
    }

    environment {
        DOCKER_IMAGE = 'abdiaoo/spring-rest-api-jenkins:latest'
    }

    stages {
        stage('Build') {
            steps {
                script {
                    sh 'mvn -B -DskipTests clean install'
                }
            }
        }

        stage("Test") {
            steps {
                script {
                    sh 'mvn -B test'
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline succeeded! You can add additional steps or notifications here.'
        }
        failure {
            echo 'Pipeline failed! You can add additional steps or notifications here.'
        }
    }
}
