#!/usr/bin/env bash
set -euo pipefail

JAR="target/testProject.jar"
LOG="app.log"
PIDFILE=".app.pid"

# 1. Build — abort everything if it fails
mvn package

# 2. Start app in background, log to file, save real java PID
java -jar "$JAR" > "$LOG" 2>&1 &
APP_PID=$!
echo "$APP_PID" > "$PIDFILE"
echo "App started (PID $APP_PID), logging to $LOG"

# a
# 3. Kill both java and tail on exit (Ctrl+C, kill, script end)
cleanup() {
    echo
    echo "Shutting down (PID $APP_PID)..."
    kill "$APP_PID" 2>/dev/null || true
    rm -f "$PIDFILE"
}
trap cleanup EXIT INT TERM

# 4. Wait for log file to exist
for i in $(seq 1 20); do
    [ -f "$LOG" ] && break
    sleep 0.1
done

# 5. Tail the log; exits when app dies
tail -f "$LOG" --pid="$APP_PID"
