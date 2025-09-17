pipeline {
    agent any

    tools {
        maven 'Maven_3.9.11'
        jdk 'JDK_17'
    }

    triggers {
        cron('H/10 6-18 * * 1-5')
    }

    stages {
        stage('Clean Workspace') {
            steps {
                // Clean old workspace before checkout
                deleteDir()
            }
        }

        stage('Checkout Code') {
            steps {
                git url: 'https://github.com/lankaprabhudeva/Resumuploadtonaukari.git', branch: 'main'
            }
        }

        stage('Verify Resume') {
            steps {
                script {
                    def resumePath = "$WORKSPACE/src/test/resources/Resume/Prabhudeva_Resume.pdf"
                    if (!fileExists(resumePath)) {
                        error "❌ Resume file not found at ${resumePath}. Build failed!"
                    } else {
                        echo "✅ Resume file found: ${resumePath}"
                    }
                }
            }
        }

        stage('Build & Test Project') {
            steps {
                // List the Resume folder to confirm the PDF is present
                sh "ls -l $WORKSPACE/src/test/resources/Resume/"

                // Run Maven build with the resume path
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
