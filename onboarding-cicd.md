- Pilot phase:
  - Start with 1-5 pilot teams, taking care of selecting important projects with a business incentive for frequent production deployments BUT not mission critical ones. Try to focus on various technologies.
## Don’t do all 5 at the same time
## The teams should feel the incentive to invest in the pipeline
## Start writing an onboarding process / user manual for the CD tooling
-          Onboarding phase. After the pilots, for the next 50 team / projects:
o    Set up an onboarding team, note that these people should not be the same as the architects of the pipeline, more people that are concise about following a document/process. They don’t need to be CI/CD experts. In fact: they shouldn’t, because if they are they will not follow the documentation hence you will not know the quality of your documentation.
o    This teams takes a project by the hand and onboards them to the new CI/CD tooling. That might include: requesting SCM repositories, configuring Jenkins, setting up authorization, setting up XLD/XLR.. doing all the heavy / annoying lifting. These people will grease the onboarding by making the time investment for the teams as minimal as possible.
o    Let these people interact with the pipeline design team often, seeing how to address and/or onboard new issues, evolving the pipeline continuously
o    Document, document, document.
-          Maintenance phase. After the first 55 projects / teams.
o    Onboarding team will dissolve, focusing on finishing documentation and not taking teams by the hand anymore. The documentation will be mature enough to support the future teams.
o    Change process for templates, plugins, documentation, etc
o    Maintenance process to keep CI/CD tooling up to date (Jenkins/XLD/XLR upgrades)
