pipeline {
    agent any   // run on any available Jenkins agent

    tools {
        maven 'Maven_3.9.11'   // must match Jenkins Global Tool Configuration
        jdk 'JDK_17'           // must match Jenkins Global Tool Configuration
    }

    triggers {
        // Run every 10 min between 6 AM - 6 PM, Mon-Fri
        cron('H/10 6-18 * * 1-5')
    }

    stages {
        stage('Checkout Code') {
            steps {
                git url: 'https://github.com/lankaprabhudeva/Resumuploadtonaukari.git', branch: 'main'
            }
        }

        stage('Build & Test Project') {
            steps {
                // This will build AND run your TestNG tests
                sh "mvn clean install -Dresume.path=$WORKSPACE/src/test/resources/Resume/Prabhudeva_Resume.pdf"
            }
        }

        stage('Archive Reports & Screenshots') {
            steps {
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
