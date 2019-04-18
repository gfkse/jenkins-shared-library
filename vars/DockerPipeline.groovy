import com.gfk.jenkins.components.AuthenticationAWS
import com.gfk.jenkins.components.Configuration
import com.gfk.jenkins.components.DockerBuildImage

def call() {
    def environmentVariables = new Configuration(this)
    def awsAuthenticator = new AuthenticationAWS(this)
    def buildDockerImage = new DockerBuildImage(this)
    pipeline {
        agent any
        options {
            buildDiscarder(logRotator(numToKeepStr: '25'))
            timeout(time: 10, unit: 'MINUTES')
            ansiColor('xterm')
            timestamps()
        }
        environment {
            BRANCH_NAME = "${BRANCH_NAME}"
        }
        stages {
            stage("env") {
                steps {
                    script {
                        environmentVariables.injectVars()
                    }
                }
            }
            stage('login') {
                steps {
                    script {
                        awsAuthenticator.logMeIn()
                    }
                }
            }
            stage('build and push image') {
                steps {
                    script {
                        buildDockerImage.buildMe()
                    }
                }
            }
        }
        post {
            always {
                deleteDir()
            }
        }
    }
}
