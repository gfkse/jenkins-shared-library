package com.gfk.jenkins.components

class DockerBuildImage implements Serializable {
    def steps
    DockerBuildImage(steps) { this.steps = steps }
    def buildMe(buildPath) {
        def dockerImage
        def buildParams = [
                "--no-cache",
                "--rm",
                "--build-arg TIME=${steps.env.TIME}",
                "--build-arg REPO=${steps.env.REPO}",
                "--build-arg HUDSON_URL=${steps.env.HUDSON_URL}",
                "--build-arg GIT_URL=${steps.env.GIT_URL}",
                "--build-arg SHORT_SHA=${steps.env.SHORT_SHA}"
        ]
        def path = buildPath ? "${buildParams.join(" ")} -f ${buildPath} ." : "${buildParams.join(' ')} ."

        dockerImage = steps.docker.build(
                "${Configuration.AWS_ACCOUNT}.dkr.ecr.${Configuration.AWS_REGION}.amazonaws.com/${steps.env.PROJECT.toLowerCase()}/${steps.env.REPO}:${steps.env.TAG}",
                "${path}"
        )
        dockerImage.push("${steps.env.TAG}")
        dockerImage.push("latest")
        steps.currentBuild.description = "${Configuration.AWS_ACCOUNT}.dkr.ecr.${Configuration.AWS_REGION}.amazonaws.com/${steps.env.PROJECT.toLowerCase()}/${steps.env.REPO}:${steps.env.TAG}"
    }
}
