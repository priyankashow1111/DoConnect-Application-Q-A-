#!/bin/bash
# =============================================================
# health_check.sh — MetroRide System Health Check
# Usage: bash health_check.sh
# =============================================================

DIVIDER="=============================================="

echo "$DIVIDER"
echo "  MetroRide — System Health Check"
echo "  $(date '+%Y-%m-%d %H:%M:%S')"
echo "$DIVIDER"

# --- 1. Disk Usage ---
echo ""
echo "[ DISK USAGE ]"
df -h --output=source,size,used,avail,pcent,target | grep -v tmpfs | grep -v udev
echo ""

# --- 2. Memory Usage ---
echo "[ MEMORY USAGE ]"
free -h | awk '
NR==1 { printf "%-12s %8s %8s %8s\n", $1, $2, $3, $4 }
NR==2 { printf "%-12s %8s %8s %8s\n", $1, $2, $3, $4 }
NR==3 { printf "%-12s %8s %8s %8s\n", $1, $2, $3, $4 }
'
echo ""

# --- 3. Running Java Processes ---
echo "[ RUNNING JAVA PROCESSES ]"
JAVA_PROCS=$(ps aux | grep '[j]ava' | awk '{print $2, $11, $12, $13}')
if [ -z "$JAVA_PROCS" ]; then
    echo "  No Java processes currently running."
else
    echo "  PID   COMMAND"
    echo "$JAVA_PROCS" | while read line; do
        echo "  $line"
    done
fi

echo ""
echo "$DIVIDER"
echo "  Health check complete."
echo "$DIVIDER"
