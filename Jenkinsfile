pipeline {
    agent {
        node {
            label 'spring-rest-api-agent'
        }
    }
    triggers {
        pollSCM 'H/2 * * * *'
    }
    tools {
        jdk 'java-17'
        maven 'jenkins-maven'
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
            agent any
            steps {
                script {
                    sh 'python3 --version'
                    sh 'docker --version'
                }
            }
        }
        // stage('Build Image') {
        //     steps {
        //         script {
        //             sh 'docker build -t abdiaoo/spring-rest-api-jenkins:latest .'
        //         }
        //     }
        // }
        // stage('Push to DockerHub') {
        //     steps {
        //         script {
        //             withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_CREDENTIALS', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
        //                 sh "docker login -u $DOCKERHUB_USERNAME -p $DOCKERHUB_PASSWORD"
        //                 sh "docker tag abdiaoo/spring-rest-api-jenkins:latest $DOCKER_IMAGE_NAME:latest"
        //                 sh "docker push $DOCKER_IMAGE_NAME:latest"
        //             }
        //         }
        //     }
        // }
    }
}
