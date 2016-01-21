Madura Perspectives
===================

Provides a web based framework for sub-applications to plug into. The framework looks after logging in and assigning permissions as well as managing a window (using Vaadin) for the sub-applications to display in. It also provides a blackboard arrangement so the sub-applications can be loosely coupled.

Sub-applications can be added dynamically while the application is running. They can contribute entries to the main menu which appear dynamically when that sub-application is active.

The framework s easily customisable using Vaadin's facilities (CSS etc). The CSS definitions are, of course, shared across
all of the sub-applications so you get a consistent look and feel.

If you want to try this out quickly there is an [on line demo](http://perspectivesmanager-madura.rhcloud.com/)

Build this using maven clean package. You will find the following sub projects:

# Subprojects

## madura-perspectives-library

This holds the code common to the perspectives manager and the sub-applications.

## madura-perspectives-manager

This is the core project and it delivers a war file that can accept sub-applications. The default is to add them to the WEB-INF/bundles directory, which at first glance makes the whole dynamic sub-applications seem a bit pointless since they are embedded in the war file. But this is a good way to set up a demo because it saves you having to configure it. See the docs for details on configuration, but you don't need to configure it to see the demo. The docs are in the pdf file in the target directory (after you've run the build).

We have tested the war file under Tomcat 7 but it should run on any JEE server.

Because it is a demo we have avoided the need to a database etc.

## madura-perspectives-name

This is the simplest of the sub-applications. It puts a single field on the screen. It also puts the field onto the blackboard.

## madura-perspectives-nameaddress

This slightly more complex in that it adds address information, as well as adding an entry to the main menu.

## madura-perspectives-pizzaorder

This is much more complex in that it implements the Pizza Order demo from [Madura Rules Demo](https://github.com/RogerParkinson/madura-vaadin-support/tree/master/madura-rules-demo). This is a rules-driven pizza configuration which lets you pick different pizza bases, toppings and sizes, except that only some combinations are allowed and the rules enforce this. The rules, UI and underlying domain objects are all delivered by the sub-application.

It also contributes two entries to the main menu: Save and Cancel, and the Save option disables if there is a required field that has not been filled in.

## madura-perspectives-bmi

The BMI calculator is a rules driven sub-application that prompts for fields as it needs them. It is the same as the BMI demo in [Madura Rules Demo](https://github.com/RogerParkinson/madura-vaadin-support/tree/master/madura-rules-demo)but packaged in a sub-application.