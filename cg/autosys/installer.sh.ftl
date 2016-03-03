
<#assign container = deployed.container>

<#assign envsJsonPath="${container.configDir}/${container.environmentName}/${deployed.applicationName}/envs.json">



export JAVA_HOME=${container.javaHome}

cd ${container.toolDir}

./asinst.sh validate ${container.autosysInstanceName} ${container.autosysHost} ${container.autosysPort} ${deployed.file} ${envsJsonPath} ${container.environmentName}
