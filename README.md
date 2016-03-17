# xld-workspace
hack repo to sync between desktop and virtual server

# design decisions
The package is going to contain XL Deploy placeholders, probably with the common {{KEY}} format, but maybe not. We'll find out soon.

Some placeholder will have a specific recognizable string within the key, like: NAMESPACE_VMACHINE_N_SUBKEY where NAMESPACE is string that has no underscore characters in it, VMACHINE is the static string that XL Deploy will recognize the key with, N is a integer with the index (let's say max 2 numbers) and SUBKEY is either NAME, HOST, MAX_LOAD or FACTOR. XL Deploy will retrieve all these keys and will generate a Vmachine definition for each NAMESPACE with the properties in SUBKEY.

Example:
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

will generate
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
(replaceing the container properties with the relevant values, obviously)
