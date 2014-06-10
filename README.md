# 3A Setup


Multi-tenant Web user identity managment

## Hardware-Software

### System requirement
   * Pentium 4 and above processor.
   * HDD 50 GB or more.
   * OS - Linux preferably Ubuntu.

### Software requirement
       
   * JDK 1.7 [download it from here](http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-javase6-419409.html)
   * Apache maven for more details [refer](http://maven.apache.org/) and [download latest binary](http://maven.apache.org/download.html) currently it is 3.2.1
   * Eclipse [download it from here](http://www.eclipse.org/downloads/) use latest version (Kepler).
   * Memecached [refer](http://memcached.org/). On ubuntu you can install it using "sudo apt-get install memcached"
   * You need to install 3 Eclipse plugins. 
      - Install GWT eclipse plugin Help -> "Eclipse Marketplace". Search with string "GWT" install "Google Plugin for eclipse 4.3".
      - Install maven plugin open "Eclipse Marketplace" and search for "m2e-wtp" look for "Maven Integration for Eclipse WTP Juno (1.0.1)" and install it
      - Other maven integration plugins open "Eclipse marketplace" and search for "m2e" look for "APT M2E Connector" and "GWT M2E Connector" install both the plugins.
Once all of the above plug-ins are installed [refer to import project into eclipse workspace](http://www.pandurangpatil.com/2014/03/install-eclipse-maven-plugin-and-import.html).
      - Install mercurial plug-in using http://cbes.javaforge.com/update to synchronise code with central repository. For more details [refer](http://www.javaforge.com/project/HGE)
   * Install MySQL 5.5 version [refer](http://dev.mysql.com/downloads/mysql/5.5.html).
