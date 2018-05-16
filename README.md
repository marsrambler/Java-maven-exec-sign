# maven-executable-jar-skeleton-proj
A demo project contains below features.
1. How to install a local jar to local maven repository.
2. How to build a executable .jar with all dependency .jar(s) embedded in.
3. How to maven plugin(s) to download source code/java doc and also attach them in maven life cycle.
4. How to create CA, Server, Client private key, pubic key, keystore, truststore....
5. How to signing a string with private key (a .jks or .p12 certificate contains both private and public key).
6. How to verify the signed string with public key (a .jks or .p12 certificate contains only public key).
7. How to signing a .jar package.
8. How to verify a signed .jar package.

Steps:
1. run install-jar.ksh
2. run cert.ksh
3. run signing & verify
4. run sign-jar.ksh
5. run jar verify
