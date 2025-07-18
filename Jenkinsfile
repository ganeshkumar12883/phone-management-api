pipeline {
    agent any

    options {
        timestamps()
    }

    stages {
        stage ('Checkout') {
            steps {
                checkout scm
            }
        }

        stage ('Build') {
            steps {
                echo 'Starting the build...'
                sh './gradlew clean build'
            }
        }

        stage('Test') {
            steps {
                echo 'Running unit and integration tests...'
                sh './gradlew test'
            }
        }

        stage ('Test Coverage Report') {
            steps {
                echo 'Generating the Test Coverage Report...'
                sh './gradlew jacocoTestReport'
            }
        }

        stage ('Archive Artifacts') {
            steps {
                echo 'Archiving the artifacts...'
                archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
            }
        }
    }

    post {
        always {
            coverage adapters : [jacocoAdapter('build/reports/jacoco/test/jacocoTestReport.xml')]
            echo 'Cleaning the workspace...'
            cleanWs()
        }
    }
}