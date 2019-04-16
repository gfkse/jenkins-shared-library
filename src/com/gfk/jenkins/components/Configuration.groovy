package com.gfk.jenkins.components

class Configuration {
    def steps
    Configuration(steps) {this.steps = steps}
    // branches that jenkins will push
    public static DEPLOY_BRANCH_PATTERN_REGEX_MATCH = '^(master|develop|release/.*|feature/.*|hotfix/.*|PR-.*)$'
    public static AWS_ACCOUNT = "123456789123"
    public static AWS_REGION = "us-east-1"
    public static FULL_ECR_URL = "${AWS_ACCOUNT}.dkr.ecr.${AWS_REGION}.amazonaws.com/${steps.env.PROJECT.toLowerCase()}/${steps.env.REPO}:${steps.env.TAG}"
    def injectVars() {
        steps.env.PROJECT = steps.sh(script: 'echo $JOB_NAME | cut -d "/" -f 1', returnStdout: true).trim()
        steps.env.REPO = steps.sh(script: 'echo $JOB_NAME | cut -d "/" -f 2', returnStdout: true).trim()
        steps.env.SHORT_SHA = steps.sh(script: 'echo $GIT_COMMIT | cut -c 1-10', returnStdout: true).trim()
        steps.env.TIME = steps.sh(script: 'date +%Y-%m-%d_%H%M%S', returnStdout: true).trim()
        steps.env.TAG = steps.sh(script: 'echo ${BRANCH_NAME}-${SHORT_SHA} | sed "s+/+-+g"', returnStdout: true).trim()
    }
}
