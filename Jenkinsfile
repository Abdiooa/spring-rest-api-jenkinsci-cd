pipeline {
    agent {
        node {
            label 'forspring-rest-api-agent'
        }
    }
    triggers {
        pollSCM 'H/2 * * * *'
    }
    tools {
        jdk 'java-17'
        maven 'jenkins-maven'
    }

    environment {
        DOCKER_IMAGE = 'abdiaoo/spring-rest-api-jenkins:latest'
    }

    stages {
        stage('Build') {
            steps {
                script {
                    sh 'mvn -B -DskipTests clean package'
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

        stage('Check Docker') {
            steps {
                script {
                    // Assuming Dockerfile is present in the project root
                    dockerImage = docker.build("${DOCKER_IMAGE}", "--file Dockerfile .")
                }
            }
        }

        stage('Push to DockerHub') {
            steps {
                script {
                    withDockerRegistry([credentialsId: "dockerhubaccount", url: ""]) {
                        dockerImage.push()
                    }
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
