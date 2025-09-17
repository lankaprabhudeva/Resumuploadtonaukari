pipeline {
    agent any

    tools {
        maven 'Maven_3.9.11'
        jdk 'JDK_17'
    }

    triggers {
        cron('H/10 6-18 * * 1-5') // Every 10 min Mon-Fri 6AM-6PM
    }

    stages {
        stage('Checkout Code') {
            steps {
                git url: 'https://github.com/lankaprabhudeva/Resumuploadtonaukari.git', branch: 'main'
            }
        }

        stage('Build & Test Project') {
            steps {
                sh "ls -l $WORKSPACE/src/test/resources/Resume/"
                sh "mvn clean install -B -Dresume.path=$WORKSPACE/src/test/resources/Resume/Prabhudeva_Resume.pdf"
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
