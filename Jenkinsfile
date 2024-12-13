@Library('shared-libraries') _

def appName = 'proximity-finder'
def availableServices = ["authservice", "eurekaserver", "management", "provider-profile-service", "gatewayserver", "request-management", "help-and-support"]
def changedServices = []

def awsCreds = [
    region: 'eu-west-1',
    iamCredId: 'aws-cred-training-center'
]

def deployConfig = [
    main: [
        revisionTag: appName,
        revisionLocation: 'gtp-artifacts-2',
        assetsPath: "app/",
        codeDeployAppName: 'gtp',
        codeDeployGroup: appName
    ],
    devops: [
        revisionTag: appName,
        revisionLocation: 'gtp-artifacts-2',
        assetsPath: "app/",
        codeDeployAppName: 'gtp',
        codeDeployGroup: appName
    ],
    develop: [
        revisionTag: appName,
        revisionLocation: 'gtp-artifacts-2',
        assetsPath: "app/",
        codeDeployAppName: 'gtp',
        codeDeployGroup: appName
    ]
]

def sharedBranches = ['main', 'develop', 'devops']

def runMavenCommand(command) {
    def mvn = tool 'maven'
    try {
        sh "${mvn}/bin/mvn ${command}"
    } catch (Exception e) {
        echo "Maven command '${command}' failed: ${e.getMessage()}"
        throw e
    }
}

def prepareDeploymentFiles(imageRegistry, gitSha, imageName, changedServices) {
    // Convert changedServices to a space-separated string
    def servicesString = changedServices.collect { "\"$it\"" }.join(' ')
    echo "Preparing deployment files for services: ${changedServices.join(', ')}"
    echo "Services string: ${servicesString}"

    // Create and set up the app directory
    echo "Creating the 'app/' directory and copying required files..."
    sh 'mkdir -p app/'
    sh "cp ./docker-compose.yml app/"
    sh "cp -r ./deploy-scripts/ app/"
    sh "cp ./appspec.yml app/"

    // Update the boot script
    echo "Updating the boot script with the list of changed services..."
    sh "sed -i 's|services=()|services=(${servicesString})|g' app/deploy-scripts/boot.sh"

    // Update docker-compose.yml with new image references
    echo "Updating docker-compose.yml with new image tags..."
    for (service in changedServices) {
        def oldImage = "909544387219.dkr.ecr.eu-west-1.amazonaws.com/${service}:latest"
        def newImage = "${imageRegistry}/${imageName}:${service}-${gitSha}"
        echo "Updating service '${service}' image: ${oldImage} -> ${newImage}"
        sh "sed -i 's|image: ${oldImage}|image: ${newImage}|g' app/docker-compose.yml"
    }

    sh "cat app/docker-compose.yml"

    echo "Deployment files prepared successfully in 'app/' directory."

}

pipeline {
    agent any
    tools {
        maven 'maven'
        jdk 'jdk21'
    }

    environment {
            currentBranch = "${env.BRANCH_NAME}"
            gitUser = sh(script: 'git log -1 --pretty=format:%ae', returnStdout: true).trim()
            gitSha = sh(script: 'git log -n 1 --pretty=format:"%H"', returnStdout: true).trim()
            imageRegistry = '909544387219.dkr.ecr.eu-west-1.amazonaws.com'
            imageName = 'proximity-finder'
            dirName = 'promixity-finder'
    }

    stages {
        stage('Identify Changed Services') {
            steps {
                script {
                    // Get the last merge commit SHA
                    echo "Identifying the last merge commit..."
                    def lastMergeCommit = sh(
                        script: """#!/bin/bash
                            git log --merges --pretty=format:'%H' -n 2 origin/${currentBranch} | sed -n '2p'
                        """,
                        returnStdout: true
                    ).trim()

                    if (!lastMergeCommit) {
                        error "Failed to identify the last merge commit. Ensure the branch '${currentBranch}' has merge commits."
                    }

                    echo "last merge commit sha: ${lastMergeCommit}"

                    // Detect changes between the last merge commit and HEAD
                    echo "Detecting changed files since last merge commit..."
                    detectedChanges = sh(script: """#!/bin/bash
                        git diff --name-only ${lastMergeCommit}...HEAD | sort -u
                    """, returnStdout: true).trim().split("\n")

                    if (!detectedChanges) {
                        echo "No changes detected in the repository."
                        changedServices = []
                        return
                    }

                     // Split changes into a list
                    echo "Detected changes:\n${detectedChanges.join('\n')}"

                    // Extract services from the changed file paths
                    echo "Identifying services from changed files..."
                    def servicesInChanges = detectedChanges.collect { change ->
                        echo "change ${change}"
                        def parts = change.split('/')
                        parts.size() > 1 ? parts[0] : null
                    }.findAll { it }

                    echo "Workspace path is: ${WORKSPACE}"

                    // Dynamically identify all service directories based on defined criteria
                    def serviceDirectories = []
                    def files = findFiles(glob: '**/pom.xml') // Adjust to match your structure
                    files.each { file ->
                        def serviceDir = file.path.tokenize('/')[0] // Extract the top-level directory
                        if (!serviceDirectories.contains(serviceDir)) {
                            serviceDirectories.add(serviceDir)
                        }
                    }

                    echo "Detected service directories: ${serviceDirectories}"

                    // Filter only available services
//                     echo "Filtering to identify changed services from available ones..."
//                     changedServices = servicesInChanges.unique().findAll { service ->
//                         availableServices.contains(service)
//                     }

                    // Filter only available services
                    // Find intersection between detected changes and identified service directories
                    echo "Filtering to identify changed services..."
                    changedServices = servicesInChanges.unique().findAll { service ->
                        serviceDirectories.contains(service)
                    }

                    // Log the final list of changed services
                    if (changedServices) {
                        echo "Changes detected in services: ${changedServices}"
                    } else {
                        echo "No changes detected in available services."
                    }

                    echo "Changed services to build: ${changedServices}"
                }
            }
        }

        stage('mvn Clean') {
             steps{
                script {
                    echo "Starting Maven clean for changed services: ${changedServices.join(', ')}"
                    changedServices.each { service ->
                        dir("./${service}"){
                            echo "current workdir: "
                            sh 'pwd'
                            echo "Running Maven clean for ${service}..."
                            runMavenCommand('clean')
                        }
                    }
                    echo "Maven clean completed for all changed services."
                }
             }
        }

        stage('mvn Validate') {
             steps{
                script {
                    echo "Starting Maven validate for changed services: ${changedServices.join(', ')}"
                    changedServices.each { service ->
                        dir("./${service}"){
                            echo "current workdir: "
                            sh 'pwd'
                            runMavenCommand('validate')
                        }
                    }
                    echo "Maven validate completed for all changed services."
                }
             }
        }

        stage('Build Changed Services') {
             when {
                expression { return sharedBranches.contains(env.BRANCH_NAME) }
            }
            steps {
                script {
                    changedServices.each { service ->
                        stage("Build ${service}") {
                            echo "Building ${service}..."
                            buildDockerImage(imageTag: "${imageRegistry}/${imageName}:${service}-${gitSha}", buildContext: "./${service}")
                        }
                    }
                }
            }
        }

        stage('Push Changed Services To ECR') {
            when {
              expression { return sharedBranches.contains(env.BRANCH_NAME) }
            }
            steps {
                script {
                    changedServices.each { service ->
                        stage("Build ${service}") {
                            echo "Pushing ${service}..."
                            pushDockerImage(image: "${imageRegistry}/${imageName}:${service}-${gitSha}", registry: imageRegistry, awsCreds: awsCreds)
                        }
                    }
                }
            }
        }

        stage('Prepare Deploy Changed Services') {
            when {
              expression { return sharedBranches.contains(env.BRANCH_NAME) }
            }
            steps {
                script {
                    prepareDeploymentFiles(imageRegistry, gitSha, imageName, changedServices)
                    prepareToDeployECR(environment: currentBranch, deploymentConfig: deployConfig, awsCreds: awsCreds)
                }
            }
        }

        stage('Deploying Changed Services') {
            when {
                  expression { return sharedBranches.contains(env.BRANCH_NAME) }
            }
            steps {
                script {
                    makeDeploymentECR(environment: currentBranch, deploymentConfig: deployConfig, awsCreds: awsCreds)
                }
            }
        }
    }
    post {
        always {
            echo "Pipeline execution completed."
            script {
                  echo "Starting cleanup tasks..."
                  try {
                    echo "Removing Docker images for changed services: ${changedServices.join(', ')}"
                    changedServices.each { service ->
                    sh "docker rmi ${imageRegistry}/${imageName}:${service}-${gitSha}"
                    echo "Removed image: ${imageRegistry}/${imageName}:${service}-${gitSha}"
                    }
                    sh 'docker system prune -f'
                    cleanWs()
                  } catch (Exception e) {
                    echo "Cleanup failed: ${e.getMessage()}"
                  }
            }
        }
        success {
          echo "Successful 😊"
        }
        failure {
          echo "Failed 😒"
        }
    }

}