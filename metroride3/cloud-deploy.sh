#!/bin/bash
# =============================================================
# cloud-deploy.sh — Upload MetroRide JAR to AWS S3
# Task 6: Cloud Storage bucket + artifact upload
#
# Prerequisites:
#   aws cli installed: pip install awscli
#   Configured:        aws configure
# =============================================================

set -e

BUCKET_NAME="metroride-artifacts-$(date +%Y%m%d)"
REGION="ap-south-1"
JAR_PATH="target/metroride-api-1.0.0.jar"
S3_KEY="releases/v1.0.0/metroride-api-1.0.0.jar"

echo "==== MetroRide Cloud Deployment ===="
echo "Region : $REGION"
echo "Bucket : $BUCKET_NAME"
echo "Artifact: $JAR_PATH"
echo ""

# Step 1: Create S3 bucket
echo "[1/3] Creating S3 bucket..."
aws s3api create-bucket \
  --bucket "$BUCKET_NAME" \
  --region "$REGION" \
  --create-bucket-configuration LocationConstraint="$REGION"

# Step 2: Enable versioning
echo "[2/3] Enabling versioning..."
aws s3api put-bucket-versioning \
  --bucket "$BUCKET_NAME" \
  --versioning-configuration Status=Enabled

# Step 3: Upload JAR
echo "[3/3] Uploading artifact..."
aws s3 cp "$JAR_PATH" "s3://$BUCKET_NAME/$S3_KEY" \
  --metadata "version=1.0.0,app=metroride-api"

echo ""
echo "==== Upload complete ===="
echo "s3://$BUCKET_NAME/$S3_KEY"
echo ""

# Verify
aws s3 ls "s3://$BUCKET_NAME/releases/" --human-readable
