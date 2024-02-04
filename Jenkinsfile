pipeline {
    agent {
        node {
            label 'pipelineforspring-rest-api-agent'
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
    }
}
