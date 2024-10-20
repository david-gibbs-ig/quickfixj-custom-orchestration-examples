# Custom Application Messages with FIXT1.1 Session maessages

This example builds custom application messages from FIX Orchestra along with FIXT1.1 Session Messages. This is **NOT** the recommended approach

This is not the recommended approach as the FIXT1.1 published by the QuickFIX/J project can be used.
Using the QuickFIX/J FIXT1.1 artifacts is practical and does not require arcane knowledge of classes that should 
be omitted from the FIXT1.1 packages to void collision with those provided by the QuickFIX/J _base_ package. 
It should be preferable to make FIXT1.1  a responsibility of the QuickFIX/J project. 

This module may be of interest if it is necessary to customise FIXT1.1. 

This example demonstrates packaging FIXT1.1 along with the Application messages, only to demonstrate the option. 
It would be more usual for FIXT1.1 and Application messages to be managed in distinct 
modules resulting in distinct artifacts.

QuickFIX/J base, core and FIXT1.1 packages can be used with custom Application message packages. 
This allows the custom messages packages to be maintained independently of the QuickFIX/J project.

Note that in this example the Maven co-ordinates for the custom artifact are different from the QuickFIX/J
`groupId` and `artifactId` . The Java package names will be the same as QuickFIX/J.
Only one implementation of the QuickFIX/J packages should be in the classpath at compilation or run time.