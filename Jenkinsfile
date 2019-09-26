pipeline {
	agent {
		kubernetes {
			cloud resolveCloudNameByBranchName()
			label 'jenkins-slave-pod-agent'
			defaultContainer 'jdk-gradle-docker-k8s-helm'
			yamlFile 'Jenkinsfile.JenkinsSlaveManifest.yaml'
//			namespace resolveNamespaceByBranchName()
		}
	}
	options { 
		timestamps() 
	}
	environment {
		// We use this dummy environment variable to load all the properties from the designated file into environment variable (per brach)
		// This is indeed a pseudo comment for real indeed!!!
		X_EFRAT_ECHO_ECHOBE_DUMMY_ENV_VAR = assimilateEnvironmentVariables()
	}
	stages {
		stage('\u2776 setup \u2728') {//\u1F4A1
			steps {
				sh 'cp -ar ./.docker /root/.docker'
				sh 'cp -ar ./.kube /root/.kube'

				script {
					// Ensure target namespace is resolved
					resolveNamespaceByBranchName()
				}
			}
		}
		stage('\u2777 build \u2728') {//\u1F6E0
			steps {
				sh './gradlew dockerBuildAndPublish --no-daemon'
			}
		}
		stage('\u2778 package \u2728') {//\u1F4E6
			steps {
				sh './gradlew helmPackage --no-daemon'
			}
		}
		stage('\u2779 install \u2728') {//\u1F3F4
			when {
				environment name: 'CLOUD_NAME', value: 'development'
			}
			steps {
				sh './gradlew helmUpdate --no-daemon'
			}
		}
		stage('\u277A upgrade \u2728') {//\u1F3F4
			when {
				not {
					environment name: 'CLOUD_NAME', value: 'development'
				}
			}
			steps {
				sh './gradlew helmUninstall --no-daemon'
				sh './gradlew helmUpdate --no-daemon'
			}
		}
		stage('\u277B verify \u2728') {
			steps {
				sh './gradlew helmTestAndClean --no-daemon'
			}
		}
		stage('\u277C test \u2728') {//\u1F321
			steps {
				sh './gradlew test --no-daemon'
			}
		}
		stage('\u277D uninstall \u2728') {//\u1F3F3
			when {
				environment name: 'CLOUD_NAME', value: 'development'
			}
			steps {
				sh './gradlew helmUninstall --no-daemon'
			}
		}
		stage('\u277E cleanup \u2728') {
			when {
				environment name: 'CLOUD_NAME', value: 'development'
			}
			steps {
				sh "kubectl delete namespace ${env.RESOLVED_NAMESPACE}"
			}
		}
	}
	post {
		always {
			echo 'One way or another, I have finished'
		}
		success {
			echo 'I succeeeded!'
			junit 'build/test-results/**/*.xml'
		}
		unstable {
			echo 'I am unstable :/'
		}
		failure {
			echo 'I failed :('
//			archiveArtifacts artifacts: 'Jenkinsfile', fingerprint: true
		}
		changed {
			echo 'Things were different before...'
		}
	}
}

//
// Determine the applicable k8s cloud (towards Jenkins' configuration of the K8S plugin)
//
def resolveCloudNameByBranchName() {
	node {
//	node(env.NODE_NAME) {
//	node('master') {
		println "Within resolveCloudNameByBranchName() => Node name is: [${env.NODE_NAME}]"

		println "Branch name is: [${env.BRANCH_NAME}]"

		if (env.BRANCH_NAME == 'master') {
			env.CLOUD_NAME = 'production'
		} else if (env.BRANCH_NAME == 'integration') {                 
			env.CLOUD_NAME = 'staging'
		}
		else {
			env.CLOUD_NAME = 'development'		    
		}
		
		println "Resolved cloud name is: [${env.CLOUD_NAME}]"
		
		// Return the resolved cloud name
		return env.CLOUD_NAME
	}
}

//
// Determine the namespace the micro service is running in (currently the Jenkins Slave Pod is running in the default namespace)
//
def resolveNamespaceByBranchName() {
	node {
		println "Within resolveNamespaceByBranchName() => Node name is: [${env.NODE_NAME}]"

		println "Branch name is: [${env.BRANCH_NAME}]"

		// If we are on the production or staging branches return the regular name (e.g. demo4echo), else return the branch namne itself
		if (env.BRANCH_NAME == 'master' || env.BRANCH_NAME == 'integration') {                 
			env.RESOLVED_NAMESPACE = 'demo4echo'
		}
		else {
			env.RESOLVED_NAMESPACE = env.BRANCH_NAME
		}
		
		println "Resolved namespace is: [${env.RESOLVED_NAMESPACE}]"
		
		// Return the resolved namespsace
		return env.RESOLVED_NAMESPACE
	}
}

//
// Load all the properties in the per brnach designated file as environment variables
//
def assimilateEnvironmentVariables() {
//	node(env.NODE_NAME) {
//		checkout(scm) => don't need it as we'll call the function after the repository has been fetched (checkout(scm) is called in the 'agent' phase)

		println "Within assimilateEnvironmentVariables() => Node name is: [${env.NODE_NAME}]"

		def props = readProperties interpolate: true, file: 'EnvFile.properties'
		props.each {
			key,value -> env."${key}" = "${value}" 
		}
		
		println "JENKINS_SLAVE_K8S_DEPLOYMENT_CLOUD_NAME value is: [${env.JENKINS_SLAVE_K8S_DEPLOYMENT_CLOUD_NAME}]"

		return env.JENKINS_SLAVE_K8S_DEPLOYMENT_CLOUD_NAME
//	}
}
