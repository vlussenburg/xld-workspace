<#-- BEGIN FREEMARKER TEMPLATE DEFINITIONS -->

<#assign container = deployed.container>

<#-- END FREEMMARKER TEMPLATE DEFINITIONS -->

export JAVA_HOME=${container.javaHome}

cd ${container.toolDir}

<#assign command = "./asinst.sh install ${container.autosysInstanceName} ${container.autosysHost} ${container.autosysPort} ${deployed.file} ${container.tempDir}/${deployed.name}/env.json ${container.environmentName}">

echo "Executing ${command}"
${command}