#!/usr/bin/env bash

mkdir ./uploads
cd uploads
mkdir ./gw-m
mkdir ./gw-w
mkdir ./tm
mkdir ./km
mkdir ./pub
mkdir ./store

echo "Put conf directories according to their respective profile. Duplicate conf directory if runs on multiple profiles"
echo "Then run the jar with the following argument :"
pwd