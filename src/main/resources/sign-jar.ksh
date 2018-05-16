#!/bin/bash

echo "signing local jar(s)..."

jarsigner -keystore server.p12 -storetype pkcs12 PrintMessage.jar 1 -signedjar PrintMessage-signed.jar -storepass password