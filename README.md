# Installation
Drop the built JAR file in the XLDEPLOY_SERVER/plugins directory. Restart the server.

# Usage
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

For every deployment of an AutosysPackage, a script will look through the dictionaries and find VMachine definitions based on the standard that is described in Design decisions below. Note that all four properties (NAME, MAX_LOAD, FACTOR and HOST) need to be present for any given VMachine definition. So all you need to do is add the keys to the dictionary and XL Deploy will do the heavy lifting.

# Design decisions
The package is going to contain XL Deploy placeholders, probably with the common {{KEY}} format, but maybe not. We'll find out soon.

