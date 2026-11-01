# 7. Using a web server (Tomcat) for MVC deployment

The program is a very small MVC application: `ServerInfoController` (`/info`) builds a
`ServerInfo` model and forwards to `WEB-INF/views/info.jsp`. The page shows the server that is
running it, which proves the deployment worked.

## Steps to deploy on Apache Tomcat 10.1

1. **Install** a JDK (17 or newer) and download Tomcat 10.1 (the zip from tomcat.apache.org).
   Unzip it; this folder is called `CATALINA_HOME` below.
2. **Build the WAR** from this folder:
   ```
   mvn package
   ```
   This creates `target/web-server-deploy.war`.
3. **Copy the WAR** into `CATALINA_HOME\webapps`.
4. **Start the server**:
   ```
   CATALINA_HOME\bin\startup.bat        (Linux/Mac: bin/startup.sh)
   ```
   Tomcat unpacks the WAR into `webapps\web-server-deploy` and starts the application.
5. **Open** http://localhost:8080/web-server-deploy/ and click the link.
6. **Stop the server** with `CATALINA_HOME\bin\shutdown.bat`.

## Things to know

- The context path (URL prefix) is the WAR file name without `.war`. Rename the WAR to
  `ROOT.war` to serve it at `http://localhost:8080/`.
- The port is set on the `<Connector port="8080" ...>` line in `CATALINA_HOME\conf\server.xml`.
- To redeploy, replace the WAR in `webapps` (Tomcat reloads it) or delete the WAR and its
  unpacked folder to remove the application.
- Errors and `System.out` output go to `CATALINA_HOME\logs` (`catalina.*.log`, `localhost.*.log`).
- From an IDE (Eclipse / IntelliJ / NetBeans) you add the Tomcat server once and use
  *Run on Server*; the IDE does the copy for you.

## No Tomcat installed?

```
mvn package cargo:run
```

downloads Tomcat 10.1, deploys the WAR and starts it. Open http://localhost:8080/web-server-deploy/
