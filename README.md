Madura Perspectives
===================

Provides a web based framework for sub-applications to plug into. The framework looks after logging in and assigning permissions as well as managing a window (using Vaadin) for the sub-applications to display in. It also provides a blackboard arrangement so the sub-applications can be loosely coupled.

Sub-applications can be added dynamically while the application is running. They can contribute entries to the main menu which appear dynamically when that sub-application is active.

The framework s easily customisable using Vaadin's facilities (stylesheets etc).

Build this using maven clean package. You will find the following sub projects:

<ul>
<le>madura-prespectives-manager. This one builds a war file suitable for deploying in a JEE server. It has been tested under Tomcat 7 and should run fine with Eclipse WTP.</le>
<le>madura-perspectives-name is a bundled sub-application which plugs into the perspectives manager.</le>
<le>madura-perspectives-name-address. Another bundled sub-application.</le>
<le>madura-perspectives-pizzaorder. Another bundled sub-application. This one is more complex,</le>
</ul> 