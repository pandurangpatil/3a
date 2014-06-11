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


## Dev setup steps 
    All steps related to setup the dev environment are listed under this section

### Setup environment

set environment variables in .bashrc

```
JAVA_HOME = Location of JDK Installation
M2_HOME=Location of Maven installation.
PATH=$M2_HOME/bin:$JAVA_HOME/bin:$PATH
AGNIE_HOME=Location of directory where all agnie configuration files will be kept.
```

#### Setup required configuration files
Below commands will copy required configurations from source code to $AGNIE_HOME. In most of the cases you might not have to change any property while doing local dev setup. In some cases if you require to change any DB credentials or schema details or any other JPA persistence related property. It is advisable not to change those properties inside corresponding persistence.xml file. There are separate overriding persistence properties files will be copied inside $AGNIE_HOME. You should define those new properties inside these properties file. This will make sure your code repo won't have any unwanted changes. 

```
$ cd $AGNIE_HOME
$ cp -rf  <3a repo path>/code/common/src/main/configuration/* ./
$ cp -rf <3a repo path>/code/user-admin/user-session/src/main/configuration/* ./
$ cp -rf <3a repo path>/code/user-admin/user-persistence/src/main/configuration/* ./
```

#### Update configuration files

  Before you make a build you need to make some changes in configuration files copied inside $AGNIE_HOME as well as some changes are required to be done inside code.

Note: Where ever you see some string enclosed inside "< string >". There you are expected to replace the contents with your own values.

* (required before build) Update "$AGNIE_HOME/global/config/init-db.properties". You need to update all properties with your own values. These values will be used to create top application as well as very first user of the system who will be the owner and admin of top application. 
* (required before build) We are using recaptch service for captch on signup page. You need to register your account at recaptcha ( [refer](https://www.google.com/recaptcha/intro/index.html) ) for your top domain that you have configured above. Once the registration is done you need to set public and private key given by recaptcha service at "$AGNIE_HOME/global/config/global-config.properties"
* (required before build) You need to configure email account details using which system will send the mails. Mails such as account verification mail or forgot password mail. Configure those details at "<3a repo path>/code/user-persistence/src/main/resources/mail_accounts.json". Note: It is expected these account details from gmail or google apps server. As we have configured gmail server for the same. If you need to use some other server, you need to do required code change.
* It is advisable to change default DB passwords that are set in code. If you need to update the password you need to do it at following locations.
  - <3a repo path>/code/db/setupdb.sql
  - <3a repo path>/code/user-persistence/src/main/resources/META-INF/persistence.xml
  - <3a repo path>/code/user-session/src/main/resources/META-INF/persistence.xml 
  - <3a repo path>/code/user-session/src/main/resources/SessionMyBatis.properties
  - <3a repo path>/code/user-session/src/test/java/com/agnie/useradmin/session/ConnectionProvider.java 


### Build

run sql setupdb.sql from following location "<3a repo path>/code/db" - This will create required db schema with required user.


user-persistence module has dependency on user-session module. But user-session modules junit tests rely upon user-persistence module to initialize and setup db schema. Which creates the cyclic dependency if you want to make a build with all junits executed without error, as of now we have to leave with this dependency. To make a complete build with junits without error. You need to first make a build by skipping the test cases (you may quit the build while it start compiling GWT module as it takes good amount of time), build user-persistence module (this will intialize test db schema)(You have to do this build by skiping the tests for only once when you are making the build very first time on the machine), and then make a complete build from base module with junits executed.

```
$ cd <3a repo path>/code
$ mvn install -Dmaven.test.skip=true
$ cd user-persistence
$ mvn clean install
$ cd ../
$ mvn clean install
```

If all your junits are executed successfully that means dev setup is done properly.

#### Initialise DB 
   You might have seen some sql scripts need to be executed before running the build. But that doesn't initialise the DB data. Those scripts only creates schema and required users having access to those schema. This will make sure you don't have to change anything inside persistence.xml as much as possible. If required to change anything as per explained under section "Setup required configuration files" you should override those properties from configuration file. Now below commands will setup required initial data.

Note: Below commands will drop all existing tables and create fresh schema with db initialised with minimum required data. 

```
$ cd  <3a4users repo path>/code/tools/target
$ unzip tools.zip 
$ cd tools/
$ java -jar tools.jar dbinit
```

#### Deployment and execution
Once all builds are made and db is initialised, deploy the modules and test them.

Below commands will help you to deploy generated wars.
```
$ cd <jetty home>/bin
$ cp <3a repo path>/code/user-admin/userapp/target/userapp-1.0.war ../webapps/userapp.war
$ ./jetty run
```

Note: When you run application in dev mode through eclipse you will not see the 3A logo at TOP LEFT corner. But when you make a build and deploy the code in servlet container and test it, you will see Logo is being displayed.

### Eclipse Dev Setup
* It is always a good practice to use same maven installation for command line build as well as for eclipse, you will see detail instruction at [refer](http://www.pandurangpatil.com/2014/03/install-eclipse-maven-plugin-and-import.html).
* Another very important configuration is Eclipse java formatter. We have to use common eclipse formatter so that all code is formatter with same formatter and it that will make sure it wont introduce any code diffs because of different formatting style. So to import our eclipse formatter follow below instructions 
    - select "Window - > Preferences" "Preferences ... " Window will appear. Expand label "Java" from left side pane, then "Code Style" and finally select "Formatter". On the right and side pane you will see "Import" button click on it and locate our eclipse formatter at location "<3a repo path>/config/agnie-eclipse-formatter.xml" [refer](https://github.com/Agnie-Technologies/3a/blob/master/config/agnie-eclipse-formatter.xml). Click on apply and then OK.
    - Once formatter is imported every time you format a java code eclipse will apply this imported formatter. NOTE: If you are not aware of eclipse short cuts then learn them as it saves lot of time. e.g. to format the open file press "CTRL + SHIFT + f" this will format the complete file. Similarly one more shortcut to remove unwanted imports as well as to import required classes you can make use of "CTRL + SHIFT + o". You should make a habit to press these shortcuts after every save.
- To setup the project inside eclipse follow the instruction given at [refer](http://www.pandurangpatil.com/2014/03/install-eclipse-maven-plugin-and-import.html).


    
