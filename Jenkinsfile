	pipeline {
	    agent any

	    tools {
	        maven 'Maven_3.9.11'
	    }

	    stages {
	        stage('Checkout') {
	            steps {
	                git branch: 'main', url: 'https://github.com/GraceQ2023/Selenium-Test-OrangeHRMProject.git'
	            }
	        }

	        stage('Build') {
	            steps {
	                sh 'mvn clean install'
	            }
	        }

	        stage('Test') {
	            steps {
	                sh "mvn clean test"
	            }
	        }

	        stage('Reports') {
	            steps {
	                publishHTML(target: [
	                    reportDir: 'src/test/resources/extentreport',
	                    reportFiles: 'ExtentReport.html',
	                    reportName: 'Extent Report'
	                ])
	            }
	        }
	    }

	    post {
	        always {
	            archiveArtifacts artifacts: '**/src/test/resources/extentreport/*.html', fingerprint: true
	            junit 'target/surefire-reports/*.xml'
	        }

	        success {
	            emailext (
	                to: 'graceqinys@gmail.com',
	                subject: "Build Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
	                body: """


	                <p>Hello Team,</p>
	                <p>The latest Jenkins build has completed successfully.</p>
	                <p><b>Project Name:</b> ${env.JOB_NAME}</p>
	                <p><b>Build Number:</b> #${env.BUILD_NUMBER}</p>
	                <p><b>Build Status:</b> <span style="color: green;"><b>SUCCESS</b></span></p>
	                <p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
	                <p><b>Extent Report:</b> <a href="http://localhost:8080/job/${env.JOB_NAME}/HTML_20Extent_20Report/">Click here</a></p>
	                <p>Cheers,</p>
	                <p><b>Grace - Automation Team</b></p>


	                """,
	                mimeType: 'text/html',
	                attachLog: true
	            )
	        }

	        failure {
	            emailext (
	                to: 'graceqinys@gmail.com',
	                subject: "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
	                body: """


	                <p>Hello Team,</p>
	                <p>The latest Jenkins build has <b style="color: red;">FAILED</b>.</p>
	                <p><b>Project Name:</b> ${env.JOB_NAME}</p>
	                <p><b>Build Number:</b> #${env.BUILD_NUMBER}</p>
	                <p><b>Build Status:</b> <span style="color: red;"><b>FAILED ❌</b></span></p>
	                <p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
	                <p><b>Please check the logs and take necessary actions.</b></p>
	                <p><b>Extent Report (if available):</b> <a href="http://localhost:8080/job/${env.JOB_NAME}/HTML_20Extent_20Report/">Click here</a></p>
	                <p>Cheers,</p>
	                <p><b>Grace - Automation Team</b></p>


	                """,
	                mimeType: 'text/html',
	                attachLog: true
	        )
	    }
	}
}
