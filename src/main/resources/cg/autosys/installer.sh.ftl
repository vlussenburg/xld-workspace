<#-- BEGIN FREEMARKER TEMPLATE DEFINITIONS -->

<#assign container = deployed.container>

<#assign installArgs = "${container.autosysInstanceName} ${container.autosysHost} ${container.autosysPort} ${step.remoteWorkingDirectory.path}/${step.artifact.name} ${step.remoteWorkingDirectory.path}/env.json ${container.environmentName}">

<#-- END FREEMMARKER TEMPLATE DEFINITIONS -->

#export JAVA_HOME=${container.javaHome}
#
#cd ${container.toolDir}
#
#if ${deployed.installAction?string}; then
#        ./asinst.sh install ${installArgs}
#else
#        ./asinst.sh validate ${installArgs}
#fi

# Commented the rest out to see the .json file after templating
cat ${step.remoteWorkingDirectory.path}/env.json
