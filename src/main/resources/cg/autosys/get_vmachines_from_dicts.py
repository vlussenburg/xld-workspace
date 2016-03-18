
KEY_SEPARATOR = "_"

def should_run():
	for _delta in deltas.deltas:
		deployed = _delta.deployedOrPrevious
		if deployed.type == "cg.AutosysInstalledPackage":
			return True
		
	return False

def get_vmachines_from_dicts(dictionaries):
	vmachines = dict({})
	
	for dictionary in dictionaries:
		
		logger.info("Processing dictionary with name %s" % dictionary.name)
		entries =  dictionary.entries
		for key in entries:
			splits = key.split(KEY_SEPARATOR)

			if (len(splits) == 4 and 
				splits[1] == "VMACHINE" and 
				splits[3] == "NAME"):

				logger.info("Found a new VMachine name definition %s" % key)

				# Get us the prefix (MARS_VMACHINE_0) which we can use for lookups below
				splits.remove("NAME")
				prefix = KEY_SEPARATOR.join(splits)			
				
				# Get the other required properties
				logger.info("Looking up HOST, MAX_LOAD, FACTOR from the current dictionary. If this step fails, make sure that all these mandatory keys are added to the same dictionary")
				name = entries[key]
				vmachines[name] = {
					"machine": entries[prefix + KEY_SEPARATOR + "HOST"],
					"max_load": entries[prefix + KEY_SEPARATOR + "MAX_LOAD"],
					"factor": entries[prefix + KEY_SEPARATOR + "FACTOR"]
				}
				logger.info("Added %s under name '%s'" % (str(vmachines[name]), name))

	return vmachines

if should_run():
	vmachines = get_vmachines_from_dicts(deployedApplication.environment.dictionaries)
	logger.info("Sharing resolved vmachines under context attribute 'vmachines'")
	context.setAttribute("vmachines", vmachines)
else:
	logger.info("No Autosys Deployeds found in delta: not running.")
