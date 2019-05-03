package com.gfk.jenkins.components

class AuthenticationAWS implements Serializable {
    def steps
    AuthenticationAWS(steps) {this.steps = steps}
    def logMeIn() {
        if (steps.env.BRANCH_NAME =~ Configuration.DEPLOY_BRANCH_PATTERN_REGEX_MATCH) {
            steps.sh "eval `aws ecr get-login --region ${Configuration.AWS_REGION} | sed 's+-e none++'`"
            steps.sh """
            aws ecr create-repository \
            --region ${Configuration.AWS_REGION} \
            --repository-name \
            ${steps.env.PROJECT.toLowerCase()}/${steps.env.REPO} || true"""
        }
    }
}