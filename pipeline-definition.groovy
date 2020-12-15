pipeline {
    agent any
    tools {
        maven 'maven'
        jdk 'JDK11'
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'solution', url: 'https://github.com/ABlack-git/flightlog-spring-app'
            }
        }
        stage('Build') {
            steps {
                dir('.') {
                    sh "mvn --batch-mode clean install -DskipTests"
                }
            }
        }
        stage('Test') {
            when {
                not {
                    expression { params.SKIP_TESTS }
                }
            }
            steps {
                dir('.') {
                    script {
                        try {
                            sh "mvn --batch-mode test -Dmaven.test.failure.ignore=true"
                        } finally {
                            junit '**/target/surefire-reports/*.xml'
                        }
                    }
                }
            }
        }
    }
}