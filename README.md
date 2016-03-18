# Installation
Drop the built JAR file in the XLDEPLOY_SERVER/plugins directory. Restart the server.

# Usage

## Types

### Deployable: cg.AutosysInstallPackage
This deployable represents an archive with autosys job definitions. The package is going to contain XL Deploy placeholders, probably with the common {{KEY}} format, but maybe not. We'll find out soon. Upon creation (installation), the installer.sh.ftl script will be invoked on the host where the container is defined.

In the future the functionality in the installer (asint.sh) should move into this plugin so the full power of the XL Deploy repository can be leveraged instead of doing magic basic on specific dictionary key formats and values.

### Container: cg.AutosysInstaller
This containers represent the piece of middleware that allows us to push the autosys package to the target environments. Note that from the XL Deploy point of view, it deploys the package to the Installer, and the installer by itself deploys the jobs to the actual hosts. 

## Vmachine definitions
The installer needs to know which Vmachine to create a part of the deployment. All you need to do is add the Vmachine info to the dictionary in the right format and XL Deploy will do the heavy lifting and pass the right config to the installer.

This plugin works upon values entered in the dictionaries associated with the environment when a Autosys package is deployed to. The name of the dictionary entry should comply to the following format: NAMESPACE\_VMACHINE\_N\_PROPERTY where NAMESPACE is string that has no underscore characters in it (like: MARS), VMACHINE is the static string that XL Deploy will recognize the key with, N is a integer with the index (like; 0, 1) and PROPERTY is either NAME, HOST, MAX_LOAD or FACTOR. XL Deploy will retrieve all these keys and will generate a Vmachine definition for the installer.

For example, given these dictionary values:
```properties
MARS_VMACHINE_0_NAME=mars_Vmachine
MARS_VMACHINE_0_MAX_LOAD=20
MARS_VMACHINE_0_FACTOR=0.00
MARS_VMACHINE_0_HOST=aradev-v2
MARS_VMACHINE_1_NAME=mars_Vmachine2
MARS_VMACHINE_1_MAX_LOAD=25
MARS_VMACHINE_1_FACTOR=1.00
MARS_VMACHINE_1_HOST=araqa-v2
```

XL Deploy will generate
```json
{
        "${container.environmentName}": {
                "config": {
                },
                "vmachines": {
                        "mars_Vmachine": {
                                "machine": "aradev-v2",
                                "max_load": "20",
                                "factor": "0.00"
                        }
                        "mars_Vmachine2": {
                                "machine": "araqa-v2",
                                "max_load": "25",
                                "factor": "1.00"
                        }
                }
        }
}
```
(replacing the container properties with the relevant values, obviously)

For every deployment of an AutosysPackage, a script will look through the dictionaries and find VMachine definitions based on this format. 

!Note that all four properties (NAME, MAX_LOAD, FACTOR and HOST) need to be present for any given VMachine definition. 

# Technical details
This plugin uses both xl-rules and synthetic modifications. 

- src/main/resources/synthetic.xml: this file adds the types and the properties to the XL Deploy type system. This allows the Deployable and the Container to be added to the repository using the CLI, REST API or GUI.
- src/main/resources/xl-rules.xml: add rules to parse the dictionaries and to generate the config json for the installer
- src/main/resources/env.json.ftl: the template for the installer config file
- src/main/resources/installer.sh.ftl: the installer wrapper script that generates the correct invocation to asinst.sh
- src/main/resources/get_vmachines_from_dicts.py: Jython script that parses the dictionaries and looks for the Vmachine definitions, store these in a variable to the env.json.ftl can generate the config based on this

Generate the JAR by installing [Maven](http://maven.apache.org/download) and Java and executing 'mvn clean install' in the root of the project.


