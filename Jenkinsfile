pipeline {
    agent { label 'ec2-agent' } // Replace with your EC2 node label

    tools {
        maven 'Maven_3.9.9'   // must match Jenkins Global Tool Configuration
        jdk 'JDK_17'          // must match Jenkins Global Tool Configuration
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

        stage('Build Project') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Run Resume Upload Test') {
            steps {
                // Use Jenkins WORKSPACE env var
                sh "mvn test -Dresume.path=$WORKSPACE/src/test/resources/Resume/Prabhudeva_Resume.pdf"
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
