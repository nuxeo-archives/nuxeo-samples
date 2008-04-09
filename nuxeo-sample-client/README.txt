Nuxeo Sample Client

This is a small example project for a client that connects
to a remote Nuxeo EP and uses its APIs. This example just
lists the names of the domains.

To build this sample, just do:

  mvn package

This will create a file target/nuxeo-sample-client-1.4.1.zip
that you can unzip somewhere. Then use run.sh or run.bat
to run the program.

You can change the Main class and use your code instead. The name
of the Main class is configured in lib/launcher.properties.

If you need more Nuxeo dependencies, you should add to the pom.xml
and update the assembly.xml to refer to them.
