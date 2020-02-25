#!/bin/bash

keytool \
    -genkey \
    -keystore keystore.jks \
    -alias ssl \
    -keyalg RSA \
    -sigalg SHA256withRSA \
    -validity 365 \
    -keysize 2048
