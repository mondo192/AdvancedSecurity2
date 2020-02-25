#!/bin/bash
javac -d bin src/ssl/*/*.java
java -cp bin ssl.server.TCPServer 3000
