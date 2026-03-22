# Parabank Automation Framework

Enterprise-grade Selenium Java + Cucumber + TestNG + API + Hybrid automation framework.

---

## 🚀 Framework Highlights

- UI Automation (Selenium + POM)
- API Automation (Java HTTP Client)
- Hybrid Testing (UI + API validation)
- Parallel Execution (ThreadLocal + TestNG)
- Retry Mechanism for flaky tests
- Extent Reports with screenshots
- Tag-based execution
- GitHub Actions CI integration
- Multi-browser matrix execution
- Dockerized execution support

---

## 🧪 Local Execution Commands

### 🔹 Smoke Suite
mvn clean test -Denv=qa -Dbrowser=chrome -Dparallel.mode=methods -Dthread.count=5 -Ddata.provider.thread.count=2 -Dretry.enabled=true -Dretry.count=2 -Dtest=com.parabank.automation.runners.SmokeTestRunner

---

### 🔹 Regression Suite
mvn clean test -Denv=qa -Dbrowser=chrome -Dparallel.mode=methods -Dthread.count=5 -Ddata.provider.thread.count=2 -Dretry.enabled=true -Dretry.count=2 -Dtest=com.parabank.automation.runners.RegressionTestRunner

---

### 🔹 UI Suite
mvn clean test -Denv=qa -Dbrowser=chrome -Dparallel.mode=methods -Dthread.count=5 -Ddata.provider.thread.count=2 -Dretry.enabled=true -Dretry.count=2 -Dtest=com.parabank.automation.runners.TestRunner

---

### 🔹 API Suite
mvn clean test -Denv=qa -Dbrowser=chrome -Dparallel.mode=methods -Dthread.count=5 -Ddata.provider.thread.count=2 -Dretry.enabled=true -Dretry.count=2 -Dtest=com.parabank.automation.runners.ApiTestRunner

---

### 🔹 Hybrid Suite
mvn clean test -Denv=qa -Dbrowser=chrome -Dparallel.mode=methods -Dthread.count=5 -Ddata.provider.thread.count=2 -Dretry.enabled=true -Dretry.count=2 -Dtest=com.parabank.automation.runners.HybridTestRunner

---

## 🏷️ Tag-Based Execution

### Run Smoke tests
mvn clean test -Dtest=com.parabank.automation.runners.RegressionTestRunner -Dcucumber.filter.tags="@smoke"

### Run Regression tests
mvn clean test -Dtest=com.parabank.automation.runners.RegressionTestRunner -Dcucumber.filter.tags="@regression"

### Run UI Smoke
mvn clean test -Dtest=com.parabank.automation.runners.TestRunner -Dcucumber.filter.tags="@ui and @smoke"

### Run API Smoke
mvn clean test -Dtest=com.parabank.automation.runners.ApiTestRunner -Dcucumber.filter.tags="@api and @smoke"

### Run Hybrid Smoke
mvn clean test -Dtest=com.parabank.automation.runners.HybridTestRunner -Dcucumber.filter.tags="@hybrid and @smoke"

---

## ⚙️ GitHub Actions CI

### Workflow Location
.github/workflows/ci.yml

### Triggers
- Push
- Pull Request
- Manual (workflow_dispatch)
- Nightly schedule

### Manual Inputs
- suite
- execution_mode
- browser
- browser_matrix
- env
- retry_count
- thread_count
- data_provider_thread_count
- parallel_mode
- headless

---

## 🐳 Docker Execution

### Docker files added
- Dockerfile
- docker-compose.yml
- docker/entrypoint.sh
- .dockerignore

### Docker browser support
- Chromium
- Firefox

### Docker smoke run (Chromium)
TEST_ENV=qa TEST_RUNNER=com.parabank.automation.runners.SmokeTestRunner PARALLEL_MODE=none THREAD_COUNT=1 DATA_PROVIDER_THREAD_COUNT=1 RETRY_COUNT=1 docker compose --profile chromium up --build --abort-on-container-exit --exit-code-from test-runner-chromium

### Docker regression run (Chromium)
TEST_ENV=qa TEST_RUNNER=com.parabank.automation.runners.RegressionTestRunner PARALLEL_MODE=methods THREAD_COUNT=5 DATA_PROVIDER_THREAD_COUNT=2 RETRY_COUNT=2 docker compose --profile chromium up --build --abort-on-container-exit --exit-code-from test-runner-chromium

### Docker smoke run (Firefox)
TEST_ENV=qa TEST_RUNNER=com.parabank.automation.runners.SmokeTestRunner PARALLEL_MODE=none THREAD_COUNT=1 DATA_PROVIDER_THREAD_COUNT=1 RETRY_COUNT=1 docker compose --profile firefox up --build --abort-on-container-exit --exit-code-from test-runner-firefox

### Docker regression run (Firefox)
TEST_ENV=qa TEST_RUNNER=com.parabank.automation.runners.RegressionTestRunner PARALLEL_MODE=methods THREAD_COUNT=5 DATA_PROVIDER_THREAD_COUNT=2 RETRY_COUNT=2 docker compose --profile firefox up --build --abort-on-container-exit --exit-code-from test-runner-firefox

### Docker cleanup
docker compose down --remove-orphans

---

## 📊 Reports & Artifacts

#### 🌐 Hosted Reports (Recommended)

Accessible via GitHub Pages:

https://gaurav1091.github.io/ParabankAutomationFramework/

#### 📦 CI Artifacts (Downloadable)

target/surefire-reports/

test-output/reports/

test-output/logs/

test-output/screenshots/

## Docker Reports

Docker keeps target inside the container, but copies reports to:

test-output/docker-artifacts/surefire-reports/

test-output/docker-artifacts/cucumber-reports/


Also Available:

test-output/reports/

test-output/logs/

test-output/screenshots/


---

## 🧠 Recommended Execution Order

### Local
1. Smoke
2. UI
3. API
4. Hybrid
5. Regression

### CI
1. Smoke on PR
2. Matrix smoke
3. Nightly regression
4. Manual matrix regression

### Docker
1. Smoke on Chrome
2. Regression on Chrome
3. Smoke on Firefox
4. Regression on Firefox

---

## ⚠️ Notes

- Hybrid tests are stateful → should run serially
- Use tags for selective execution
- Use regression runner for full validation
- Retry mechanism handles flaky UI issues
- Multi-browser execution is handled via CI
- Docker execution uses remote WebDriver
- Docker browser support in this setup is Chrome and Firefox