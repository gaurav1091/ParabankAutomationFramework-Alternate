#!/usr/bin/env bash
set -e

TEST_ENV="${TEST_ENV:-qa}"
BROWSER="${BROWSER:-chrome}"
EXECUTION_MODE="${EXECUTION_MODE:-remote}"
SELENIUM_REMOTE_URL="${SELENIUM_REMOTE_URL:-http://selenium-chromium:4444/wd/hub}"
HEADLESS="${HEADLESS:-false}"
PARALLEL_MODE="${PARALLEL_MODE:-none}"
THREAD_COUNT="${THREAD_COUNT:-1}"
DATA_PROVIDER_THREAD_COUNT="${DATA_PROVIDER_THREAD_COUNT:-1}"
RETRY_ENABLED="${RETRY_ENABLED:-true}"
RETRY_COUNT="${RETRY_COUNT:-1}"
TEST_RUNNER="${TEST_RUNNER:-com.parabank.automation.runners.SmokeTestRunner}"
CUCUMBER_FILTER_TAGS="${CUCUMBER_FILTER_TAGS:-}"
CUCUMBER_FEATURES="${CUCUMBER_FEATURES:-}"

PARABANK_USERNAME="${PARABANK_USERNAME:-}"
PARABANK_PASSWORD="${PARABANK_PASSWORD:-}"
BROWSERSTACK_USERNAME="${BROWSERSTACK_USERNAME:-}"
BROWSERSTACK_ACCESS_KEY="${BROWSERSTACK_ACCESS_KEY:-}"

echo "=================================================="
echo "Docker Test Runner Configuration"
echo "TEST_ENV=$TEST_ENV"
echo "BROWSER=$BROWSER"
echo "EXECUTION_MODE=$EXECUTION_MODE"
echo "SELENIUM_REMOTE_URL=$SELENIUM_REMOTE_URL"
echo "HEADLESS=$HEADLESS"
echo "PARALLEL_MODE=$PARALLEL_MODE"
echo "THREAD_COUNT=$THREAD_COUNT"
echo "DATA_PROVIDER_THREAD_COUNT=$DATA_PROVIDER_THREAD_COUNT"
echo "RETRY_ENABLED=$RETRY_ENABLED"
echo "RETRY_COUNT=$RETRY_COUNT"
echo "TEST_RUNNER=$TEST_RUNNER"
echo "CUCUMBER_FILTER_TAGS=$CUCUMBER_FILTER_TAGS"
echo "CUCUMBER_FEATURES=$CUCUMBER_FEATURES"
echo "PARABANK_USERNAME provided: $([[ -n "$PARABANK_USERNAME" ]] && echo yes || echo no)"
echo "PARABANK_PASSWORD provided: $([[ -n "$PARABANK_PASSWORD" ]] && echo yes || echo no)"
echo "BROWSERSTACK_USERNAME provided: $([[ -n "$BROWSERSTACK_USERNAME" ]] && echo yes || echo no)"
echo "BROWSERSTACK_ACCESS_KEY provided: $([[ -n "$BROWSERSTACK_ACCESS_KEY" ]] && echo yes || echo no)"
echo "=================================================="

STATUS_URL="${SELENIUM_REMOTE_URL%/wd/hub}/status"

echo "Waiting for Selenium server at $STATUS_URL"

for i in $(seq 1 30); do
  if curl -s "$STATUS_URL" | grep -q '"ready"[[:space:]]*:[[:space:]]*true'; then
    echo "Selenium server is ready."
    break
  fi

  if [ "$i" -eq 30 ]; then
    echo "Selenium server did not become ready in time."
    exit 1
  fi

  echo "Selenium not ready yet. Retrying in 2 seconds..."
  sleep 2
done

MVN_CMD=(
  mvn
  clean
  test
  "-Denv=${TEST_ENV}"
  "-Dbrowser=${BROWSER}"
  "-Dexecution.mode=${EXECUTION_MODE}"
  "-Dselenium.remote.url=${SELENIUM_REMOTE_URL}"
  "-Dheadless=${HEADLESS}"
  "-Dparallel.mode=${PARALLEL_MODE}"
  "-Dthread.count=${THREAD_COUNT}"
  "-Ddata.provider.thread.count=${DATA_PROVIDER_THREAD_COUNT}"
  "-Dretry.enabled=${RETRY_ENABLED}"
  "-Dretry.count=${RETRY_COUNT}"
  "-Dtest=${TEST_RUNNER}"
)

if [ -n "$PARABANK_USERNAME" ]; then
  MVN_CMD+=("-Dapp.username=${PARABANK_USERNAME}")
fi

if [ -n "$PARABANK_PASSWORD" ]; then
  MVN_CMD+=("-Dapp.password=${PARABANK_PASSWORD}")
fi

if [ -n "$BROWSERSTACK_USERNAME" ]; then
  MVN_CMD+=("-Dbrowserstack.username=${BROWSERSTACK_USERNAME}")
fi

if [ -n "$BROWSERSTACK_ACCESS_KEY" ]; then
  MVN_CMD+=("-Dbrowserstack.access.key=${BROWSERSTACK_ACCESS_KEY}")
fi

if [ -n "$CUCUMBER_FILTER_TAGS" ]; then
  MVN_CMD+=("-Dcucumber.filter.tags=${CUCUMBER_FILTER_TAGS}")
fi

if [ -n "$CUCUMBER_FEATURES" ]; then
  MVN_CMD+=("-Dcucumber.features=${CUCUMBER_FEATURES}")
fi

echo "Executing Maven command:"
printf '%q ' "${MVN_CMD[@]}"
echo

set +e
"${MVN_CMD[@]}"
TEST_EXIT_CODE=$?
set -e

mkdir -p /app/test-output/docker-artifacts

if [ -d /app/target/surefire-reports ]; then
  rm -rf /app/test-output/docker-artifacts/surefire-reports
  cp -R /app/target/surefire-reports /app/test-output/docker-artifacts/
fi

if [ -d /app/target/cucumber-reports ]; then
  rm -rf /app/test-output/docker-artifacts/cucumber-reports
  cp -R /app/target/cucumber-reports /app/test-output/docker-artifacts/
fi

exit $TEST_EXIT_CODE
