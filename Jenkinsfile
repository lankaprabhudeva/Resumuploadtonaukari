pipeline {
    agent any

    tools {
        maven 'Maven 3.9.9'
        jdk 'JDK17'
    }

    triggers {
        // Run every 10 mins between 6 AM and 6 PM (Mon–Fri)
        cron('H/10 6-18 * * 1-5')
    }

    stages {
        stage('Checkout Code') {
            steps {
                git url: 'https://github.com/lankaprabhudeva/Resumuploadtonaukari.git', branch: 'main'
            }
        }

        stage('Build Project') {
            steps {
                bat 'mvn clean install'
            }
        }

        stage('Run Resume Upload Test') {
            steps {
                // Pass resume path dynamically from Jenkins workspace
                bat 'mvn test -Dresume.path=%WORKSPACE%\\src\\test\\resources\\Resume\\Prabhudeva_Resume.pdf'
            }
        }

        stage('Archive Reports & Screenshots') {
            steps {
                // Collect TestNG XML report
                junit '**/test-output/testng-results.xml'

                // Collect JUnit-style reports from Surefire
                junit '**/target/surefire-reports/*.xml'

                // Archive screenshots (if taken during failures)
                archiveArtifacts artifacts: '**/*.png', allowEmptyArchive: true
            }
        }
    }

    post {
        success {
            echo '✅ Resume Upload Job Successful'
        }
        failure {
            echo '❌ Resume Upload Job Failed'
        }
    }
}
