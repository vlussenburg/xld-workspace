
KEY_SEPARATOR = "_"

vmachines = dict({})
for dictionary in deployedApplication.environment.dictionaries:
	
	print "Processing dictionary with name %s" % dictionary.name
	entries =  dictionary.entries
	for key in entries:
		splits = key.split(KEY_SEPARATOR)

		if (len(splits) == 4 and 
			splits[1] == "VMACHINE" and 
			splits[3] == "NAME"):

			print "Found a new VMachine name definition %s" % key
		
			# Get us the prefix (MARS_VMACHINE_0) which we can use for lookups below
			splits.remove("NAME")
			prefix = KEY_SEPARATOR.join(splits)			
			
			# Get the other required properties
			print "Looking up HOST, MAX_LOAD, FACTOR from the current dictionary. If this step fails, make sure that all these mandatory keys are added to the same dictionary"
			name = entries[key]
			vmachines[name] = {
				"machine": entries[prefix + KEY_SEPARATOR + "HOST"],
				"max_load": entries[prefix + KEY_SEPARATOR + "MAX_LOAD"],
				"factor": entries[prefix + KEY_SEPARATOR + "FACTOR"]
			}
			print "Added %s under name '%s'" % (str(vmachines[name]), name)

print "Sharing resolved vmachines under context attribute 'vmachines'"
context.setAttribute("vmachines", vmachines)
