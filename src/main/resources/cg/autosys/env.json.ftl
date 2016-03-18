<#-- BEGIN FREEMARKER TEMPLATE DEFINITIONS -->

<#assign container = deployed.container>
<#assign vmachines = context.getAttribute("vmachines")>

<#-- END FREEMARKER TEMPLATE DEFINITIONS -->

{
        "${container.environmentName}": {
                "config": {
                },
                "vmachines": {
<#list vmachines?keys as key>
                        "${key}": {
                                "machine": "${vmachines[key].machine}",
                                "max_load": "${vmachines[key].max_load}",
                                "factor": "${vmachines[key].factor}"
                        },
</#list>
                }
        }
}
