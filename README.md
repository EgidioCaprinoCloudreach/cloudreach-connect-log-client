# cloudreach-connect-log-client
Client for the log service hosted on Cloudreach Connect.

## Install

Add the following to your pom.xml file.

``` xml
<repositories>
  <repository>
      <id>cloudreach-connect</id>
      <url>https://raw.githubusercontent.com/EgidioCaprinoCloudreach/cloudreach-connect-log-client/mvn-repo/
      </url>
  </repository>
</repositories>

<dependencies>
  <dependency>
      <groupId>com.cloudreach</groupId>
      <artifactId>connect-log-client</artifactId>
      <version>1.2</version>
  </dependency>
</dependencies>
```
## Usage

``` java
LogService logger = new CloudreachConnectLogger(SERVICE_URL, APP_KEY);
logger.info("Info message");
logger.error("Error message");
logger.error("Error message with exception stack trace", new Exception());
```
