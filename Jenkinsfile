pipeline {
    agent any

    tools {
        maven 'Maven_3.9.11'
        jdk 'JDK_17'
    }

    triggers {
        cron('H/10 6-18 * * 1-5') // runs every 10 mins between 6 AM - 6 PM Mon-Fri
    }

    stages {
        stage('Checkout Code') {
            steps {
                // Checkout main branch
                git url: 'https://github.com/lankaprabhudeva/Resumuploadtonaukari.git', branch: 'main'
            }
        }

        stage('Build & Test Project') {
            steps {
                // Verify resume file exists
                sh "ls -l $WORKSPACE/src/test/resources/Resume/"
                
                // Build and pass resume path
                sh "mvn clean install -Dresume.path=$WORKSPACE/src/test/resources/Resume/Prabhudeva_Resume.pdf"
            }
        }

        stage('Archive Reports & Screenshots') {
            steps {
                // Archive test results and screenshots
                junit 'target/surefire-reports/*.xml'
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
