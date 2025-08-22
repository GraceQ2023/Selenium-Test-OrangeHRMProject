# OrangeHRM Automation Testing Project  


## 📌 Project Overview  

This is an end-to-end automation testing project built for the [OrangeHRM Demo Site](https://opensource-demo.orangehrmlive.com/web/index.php/auth/login)
. The project demonstrates the design and implementation of a Selenium automation framework with CI/CD integration using Jenkins, Docker, and Selenium Grid.
It covers functional, cross-browser, API, and database testing, with reporting and logging integrated.


## 🛠 Tools & Technologies

* Programming Language: Java
* Automation Framework: Selenium WebDriver, TestNG, Maven
* CI/CD: Jenkins
* Containerization & Grid: Docker, Selenium Grid
* Reporting & Logging: ExtentReports, Log4j
* Version Control: Git, GitHub
* API Testing: Rest Assured
* Database Testing: MySQL


## ⚙️ Framework Features

* Page Object Model (POM) for maintainability
* Data-driven testing with Excel utilities
* Parallel & cross-browser execution (Chrome, Edge, Firefox) using Selenium Grid
* Dockerized execution for distributed test runs
* Jenkins pipeline integration with automated builds, test execution, and email notifications
* ExtentReports for detailed HTML reports
* Log4j for logging test execution
* API testing with Rest Assured
* Basic database validation with MySQL


## 🚀 How to Run

### 🔹 Run Tests Locally
Clone the repository:
git clone https://github.com/GraceQ2023/Selenium-Test-OrangeHRMProject.git
cd Selenium-Test-OrangeHRMProject

Install dependencies:
mvn clean install

Run TestNG suite:
mvn test -DsuiteXmlFile=testng.xml

### 🔹 Run with Selenium Grid (Docker)
Start Selenium Grid using Docker Compose:
docker compose -f docker/docker-compose.yml up -d

Run tests in parallel on Grid:
mvn test -DseleniumGrid=true

### 🔹 Run via Jenkins Pipeline
Configure Jenkins with Maven and JDK.
Add pipeline script (Jenkinsfile) from repository.
Trigger the build → Jenkins will spin up Selenium Grid, run tests, and publish reports.


## 📊 Reporting
* ExtentReports: Generates rich HTML reports (/test-output/ExtentReport.html).
* Email Notifications: Sent automatically after pipeline execution.


## 🎯 Key Outcomes
* Delivered a scalable automation framework supporting functional, API, and database testing.
* Enabled CI/CD integration with Jenkins for continuous testing.
* Achieved cross-browser, parallel execution with Selenium Grid and Docker.
