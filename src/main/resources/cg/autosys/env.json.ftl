{
	"${deployed.container.environmentName}": {
		"config": {
			"machine": "${deployed.applicationName}_Vmachine",
			"envPrefix": "${deployed.container.environmentName}_",
			"jobOwner": "${deployed.jobAccount}",
			"jobAppName": "${deployed.applicationName}",
			"profile" : "/users/${deployed.applicationName}/config/${deployed.applicationName}_autosys.profile"
		},
		"vmachines": {
			"${deployed.applicationName}_Vmachine": {
				"machine": "${deployed.container.firstMachine}",
				"max_load": "20",				
				"factor": "0.00"			
			}
			"${deployed.applicationName}_Vmachine2": {
				"machine": "${deployed.container.secondMachine}",
				"max_load": "20",				
				"factor": "0.00"			
			}
		}
	}
}
