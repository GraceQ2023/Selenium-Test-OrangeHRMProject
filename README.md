

## 📌 Project Overview  

This is a small end-to-end automation testing project built for the [OrangeHRM Demo Site](https://opensource-demo.orangehrmlive.com/web/index.php/auth/login)
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

### Prerequisites
1. Have the required browsers installed: Chrome, Edge, Firefox.
2. Set up the OrangeHRM demo site locally:
    - Download and install [XAMPP](https://www.apachefriends.org/download.html).
    - Download OrangeHRM from [SourceForge](https://www.orangehrm.com/en/open-source/register-to-download).
    - Start Apache and MySQL in XAMPP.
    - Create a database named `orangehrm` in phpMyAdmin.
    - Copy the OrangeHRM folder into `htdocs/`.
    - Open your browser and go to [http://localhost/orangehrm/](http://localhost/orangehrm/) to complete installation.
    - Create an admin user:
      ```
      Username: orangehrm_hverma
      Password: Rmit1234!
      ```

### 🔹 Run Tests Locally
1. Clone the repository:
   ```bash
   git clone https://github.com/GraceQ2023/Selenium-Test-OrangeHRMProject.git
   cd Selenium-Test-OrangeHRMProject
   ```

2. Install dependencies:
    ```bash
    mvn clean install
   ```

3. Run TestNG suite:
    ```bash
    mvn test -DsuiteXmlFile=testng.xml
   ```

### 🔹 Run with Selenium Grid (Docker)
1. Update config.properties to: seleniumGrid=true
2. Start Selenium Grid using Docker Compose:
    ```bash
    docker compose -f docker/docker-compose.yml up -d
   ```

3. Run tests in parallel on Grid:
    ```bash
    mvn test -DseleniumGrid=true
   ```

### 🔹 Run via Jenkins Pipeline (macOS)
1. Update config.properties to: seleniumGrid=true
2. Install Jenkins (e.g., via Homebrew: brew install jenkins-lts).
3. Unlock Jenkins using the initial admin password
    ```bash
   cat ~/.jenkins/secrets/initialAdminPassword
   ```
4. Configure Jenkins: Install Maven and JDK in Jenkins global tools
5. Create a Pipeline job:
   - Job type: Pipeline
   - Definition: Pipeline script from SCM
   - Repository URL: your GitHub repo
   - Script Path: Jenkinsfile
6. Run the Pipeline:
   - Jenkins will automatically spin up Selenium Grid, run tests, and publish reports.
   - Test results can be viewed in Jenkins and automatically sent out.


## 📊 Reporting
* ExtentReports: Generates rich HTML reports (/test-output/ExtentReport.html).
* Email Notifications: Sent automatically after pipeline execution.


## 🎯 Key Outcomes
* Delivered a scalable automation framework supporting functional, API, and database testing.
* Enabled CI/CD integration with Jenkins for continuous testing.
* Achieved cross-browser, parallel execution with Selenium Grid and Docker.
