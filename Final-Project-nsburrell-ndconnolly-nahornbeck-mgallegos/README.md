Authors: Nick Burrell, Noah Connolly, Mikey Gallegos, and Noah Hornbeck

Summary: This is a Maven integrated program that allows users to create their own chat servers and allows other clients to connect
to each server through the given server's IP address.

Use: To run the chat program, the only two classes that need to be run are the ClientUILauncher and ServerGUI classes. The server must be running in
order for the client to connect.

Potential Error: On Mac devices, when the user sends a message, an assertion failure happens due to a bug in JavaFX 10. This
bug is removed in a later version of JavaFX that is not being used for this project.

Suppressed Warnings: Most of the warnings that were suppressed are for empty catch blocks. These warnings are supperssed
because due to the nature of Input and Output streams, there must be a try/catch block for them to even work, but we don't
want the program to stop if there are no inputs or outputs. Another warning suppressed is one in the ClientGUI class that is for a
redundant assignment of a port number. This is suppressed because if it wasn't assigned there and the try didn't work, then 
the creation of the Client class would throw errors since the port number wouldn't have been initialized. A final warning suppressed
is making a for loop a foreach loop. Reasoning, foreach loops are stupid. **NEW** In the IPTest class, the suppressed
warning for the unused variables is warranted because the variables are used, they're just used to call things, so the class
doesn't count them as being used.

As of now, the program is currently not working after realizing that we would have to rebuild the entirety of it to make it compatible with Screen Builder. It was originally built with hard-coded windows, but hasx since transitioned to Screen Builder as per request of the class assignment.
