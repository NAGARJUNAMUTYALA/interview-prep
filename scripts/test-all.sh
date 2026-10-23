#!/usr/bin/env bash
set -e

echo "=========================================================="
echo " Executing Complete Interview Prep Test Suite"
echo "=========================================================="

mvn clean test
echo "✅ All tests passed successfully!"
