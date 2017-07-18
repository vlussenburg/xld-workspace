## Create application packages in XL Deploy

Creating application packages from pre-existing artifact repositories and adding metadata required to deploy (datasource specs, queue definitions). For the basics one should refer to [this guide](https://docs.xebialabs.com/xl-deploy/5.5.x/customizationmanual.html). Creating deployment packages through the UI is clearly not a scalable approach to deployment automation. The deployment packages can be automatically created using a number of options:

-	A plugin for your CI server (Bamboo, Jenkins)
-	A CLI script using the [Jython API](https://docs.xebialabs.com/jython-docs/#!/xl-deploy/5.5.x/) 
-	[The REST API](https://docs.xebialabs.com/xl-deploy/5.5.x/rest-api/) (from XL Release, PowerShell, Bash, any custom tool)

## Plugin Design and Development Practice

XL Deploy supports many types of application packages and containers, but it is not uncommon that XL Deploy needs some customization specific to the enterprise where it’s running. Examples of customizations points are:

-	Custom deployable and container types for in-house created deployable concepts
-	Custom rules for startup / shutdown of industry-standard containers

## Developing support for custom application types (deployment packages) and middleware (containers)

In order to understand the XL Deploy extension mechanisms, it’s important to know the tool a little better than just using it for deployment will teach you. We recommend going through this manual first until (not including) the “Extending XL Deploy” chapter. This manual explains essentials concepts of what moving pieces there are under the hood. Especially deployable, deployed and container are going to be used a lot in the rest of this document.

We recommend having a sandbox XL Deploy / XL Release for working on supporting new types of deployables and containers. The sandbox machine should be an unmanaged free-for-all environment. It is only to be used to develop new types, and not by any teams do actual deployments on a regular interval. It’s just for testing purposes.

### Preparing your plugin

You start your development cycle by defining a name for your plugin and requesting a git repository. Usually, the name of the plugin is derived from the container or technology stack it adds support for, like: xld-autosys-plugin or xld-websphere-plugin.  For the name of the actual plugin file that is built, we recommend the following the XL Deploy convention of \<companycode\>-xld-\<pluginname\>-plugin. The build system will take of naming the deliverable correctly.

### Setting up your tools and workspace

Since plugin development is not unlike software development, we recommend this to be done in a professional manner using development best practices like using version control, like SVN or Git, and automated repeatable builds, using Gradle or Maven and optionally building the plugin automatically using a Continuous Integration server like Bamboo or Jenkins.

There is no official recommended editor for XL Deploy/XL Release, but consider Notepad++, a decent free tool for editing Jython, XML and shell scripts or Sublime, if you're willing to get a license for it. Don’t even think about doing any work on plugins with regular notepad!

### Creating the plugin project structure

Remember that anything in the ext/ folder on the XL Deploy / XL Release sandbox environment is volatile: someone might delete, modify or overwrite your scripts and configuration. For this reason it’s essential to start right away to put your work in a plugin structure managed within a version controlled repository.

As a rule of thumb, you can copy everything from an existing plugin and then delete the contents of the src/main/resources folder. You should end up with something that looks like:
 
An empty synthetic.xml to the src/main/resources folder with the following content:
````
<?xml version='1.0' encoding='UTF-8'?>
<!--
Note: If you modify this file, you must restart the XL Deploy server.
-->
<synthetic xsi:schemaLocation="http://www.xebialabs.com/deployit/synthetic synthetic.xsd" xmlns="http://www.xebialabs.com/deployit/synthetic" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
 
</synthetic>
````

Be sure to read back in the Customization Manual if you forgot what the purpose of the synthetic.xml is!

If you're required to use maven, you can ddd a pom.xml in the root with the following content:
````
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>org.example</groupId>
  <artifactId>xld-thing-plugin</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>xld-thing-plugin</name>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>

</project>
````

Make sure to rename the plugin to the name of your choosing.

Once you finished this, you can run mvn clean install and the plugin JAR file will end up in the target/ folder in the project root. This JAR is the packaged plugin that will be placed in the plugins folder of XL Deploy later.

## Add new types to the synthetic

From your design discussion (document this discussion in the README.md in the root of your git repository) you should have identified what you are going to deploy where, in other words, the Deployable and the Container types. For example, a deployable could be a WAR (deployable) and the Container could be Tomcat (container), or another example, an AutosysInstallPackage (deployable) and an AutosysInstaller (container).

Creating (many) custom types is not necessarily best practice. Creating custom types means maintaining custom types. Depending on the amount of necessary universal properties and rules across deployment artifacts, having a custom type with these properties and rules defined ahead of time is a simple and effective way to ensure common behavior. Proper management of types will also ensure that the right artifacts land into the right containers. 

Now add the types to the XML using an editor of your choosing. You can do this in an iterative approach, starting naïvely simple and slowly building it out, keeping a working plugin every step of the way.

Some handy resources:

-	Deployable (99% of the time you’ll generate this from the Deployed, read the manual!)
-	Deployed
-	Container

## Create scripts

Writing shell scripts is often a process of trial and error. The only good way of testing a plugin is actually deploying and usually that means a dependency on complicated middleware. Usually you’ll end up having some kind of artifact which you want on the target host and an installation script to invoke. These installation scripts should be data driven. In other words, no hardcoded names, no hardcoded paths, no hardcoded server names. XL Deploy uses an open source templating engine (Freemarker) to allow you make templates for your scripts and specify which variables you can use. Resources that are very useful are:

- Templating in the Generic Plugin
- XL Deploy Variables Demystified

Although templates for scripts are part of the plugin as well, you can create a fast feedback loop by overriding the script you have in your plugin in the /ext folder of XL Deploy. Every time you edit at this location, XL Deploy will pick up the changes. This is very handy when debugging shell scripts. However, once development is finished, the script in this folder should be packaged and placed in the plugin project and the /ext folder should be cleared of said plugin code. 

## Create rules
Rules are a relatively new feature in XL Deploy. Rules are focused on defining crosscutting behavior across different types through the tool. An example of this is a pre deployment step for all deployments done.

-	Checksum validation plugin, which serves as a good example
-	Rules tutorial, how to define rules
-	Steps tutorial, the steps that can be part of the rules

Duplicate rules across plugins and xl-rules.xml will throw errors when XL Deploy starts or refreshes rules. However, XL Deploy will not fail to start or crash, making the issue less obvious. Awareness across plugins and xl-rules.xml is necessary. Plugin type, rule and resource names should have good naming practices in order to make behavior obvious. 

## Plugin promotion

Although your CI server can take care of building the plugin, there no fixed approach yet of promoting the plugin from the sandbox to the different XLD server (integration, prod, etc). It could be a manual task (via a ticket), something that is managed by provisioning (every XLD instance will get the plugin at the same time) or XLD can deploy the plugin to other XLD instances. The latter creates an obvious chicken-or-egg problem ;).
We recommend approaching plugins as provisioning.
