#!/bin/bash
mvn clean package
cp /target/*.jar /output/
