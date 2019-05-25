// Exported from:        http://fe390b91e9ae:5516/#/templates/Folder610387126-Releasebc6ff6bb8054456090481c7a4ff2e486/releasefile
// XL Release version:   8.6.1
// Date created:         Sat May 25 13:54:38 UTC 2019

xlr {
  template('PetPortalServiceNow') {
    folder('PetPortal Project')
    variables {
      stringVariable('buildNumber') {
        required false
        showOnReleaseStart false
      }
      stringVariable('ticket') {
        required false
        showOnReleaseStart false
      }
      stringVariable('changeRequestId') {
        required false
        showOnReleaseStart false
      }
      stringVariable('changeRequestSysId') {
        required false
        showOnReleaseStart false
      }
      stringVariable('changeRequestStatus') {
        required false
        showOnReleaseStart false
      }
    }
    scheduledStartDate Date.parse("yyyy-MM-dd'T'HH:mm:ssZ", '2016-02-02T08:00:00+0000')
    phases {
      phase('Dev!') {
        color '#009CDB'
        tasks {
          custom('Open Change Request') {
            script {
              type 'servicenow.CreateRequest'
              servicenowServer 'servicenow'
              shortDescription 'PetPortal Demo'
              comments 'PetPortal Demo'
              sysId variable('changeRequestSysId')
              'Ticket' variable('changeRequestId')
            }
          }
          custom('Build package') {
            script {
              type 'jenkins.Build'
              jenkinsServer 'Jenkins'
              jobName 'PetPortal'
              buildNumber variable('buildNumber')
            }
          }
          custom('Deploy to TEST') {
            script {
              type 'xldeploy.Deploy'
              server 'XebiaLabs Internal'
              retryCounter 'currentContinueRetrial':'0','currentPollingTrial':'0'
              deploymentPackage 'PetPortal/2.1-${buildNumber}'
              deploymentEnvironment 'Dev/DEV'
            }
          }
          custom('Add comment to change request') {
            script {
              type 'servicenow.UpdateRecord'
              servicenowServer 'servicenow'
              sysId '${changeRequestSysId}'
              content '{\n' +
                      '"work_notes":"PetPortal/2.1-${buildNumber} available on DEV"\n' +
                      '}'
            }
          }
        }
      }
      phase('Test') {
        color '#009CDB'
        tasks {
          custom('Wait for approval for deployment to test') {
            script {
              type 'servicenow.PollingCheckStatus'
              servicenowServer 'servicenow'
              sysId '${changeRequestSysId}'
              pollInterval 10
              statusField 'approval'
              checkForStatus 'Approved'
              status variable('changeRequestStatus')
            }
          }
          custom('Deploy to TEST') {
            script {
              type 'xldeploy.Deploy'
              server 'XebiaLabs Internal'
              retryCounter 'currentContinueRetrial':'0','currentPollingTrial':'0'
              deploymentPackage 'PetPortal/2.1-${buildNumber}'
              deploymentEnvironment 'Dev/DEV'
            }
          }
          parallelGroup('Perform Automated Testing') {
            tasks {
              script('Smoke Testing') {
                owner 'admin'
                script (['''\
import time
time.sleep(5)
print "Performing Smoke Tests"
'''])
              }
              script('Load Testing') {
                owner 'admin'
                script (['''\
import time
time.sleep(10)
print "Performing  Load Tests"
'''])
              }
            }
          }
          custom('Add comment to change request') {
            script {
              type 'servicenow.UpdateRecord'
              servicenowServer 'servicenow'
              sysId '${changeRequestSysId}'
              content '{\n' +
              '"work_notes":"PetPortal/2.1-${buildNumber} available on TEST"\n' +
              '}'
            }
          }
        }
      }
      phase('PROD') {
        color '#009CDB'
        tasks {
          custom('Wait for approval for deployment to test') {
            script {
              type 'servicenow.PollingCheckStatus'
              servicenowServer 'servicenow'
              sysId '${changeRequestSysId}'
              pollInterval 10
              statusField 'approval'
              checkForStatus 'Approved'
              status variable('changeRequestStatus')
            }
          }
          custom('Deploy to PROD') {
            script {
              type 'xldeploy.Deploy'
              server 'XebiaLabs Internal'
              retryCounter 'currentContinueRetrial':'0','currentPollingTrial':'0'
              deploymentPackage 'PetPortal/2.1-${buildNumber}'
              deploymentEnvironment 'Dev/DEV'
            }
          }
          gate('Verify PROD Deployment') {
            owner 'admin'
            conditions {
              condition('Smoke Tests Successful')
              condition('Test Transaction Successful')
            }
          }
          custom('Add comment to change request') {
            script {
              type 'servicenow.UpdateRecord'
              servicenowServer 'servicenow'
              sysId '${changeRequestSysId}'
              content '{\n' +
              '"work_notes":"PetPortal/2.1-${buildNumber} available on PROD"\n' +
              '}'
            }
          }
        }
      }
    }
    extensions {
      dashboard('Dashboard') {
        tiles {
          releaseProgressTile('Release progress') {
            
          }
          releaseHealthTile('Release health') {
            
          }
          releaseSummaryTile('Release summary') {
            
          }
          resourceUsageTile('Resource usage') {
            row 1
          }
          timelineTile('Release timeline') {
            row 2
          }
          serviceNowQueryTile('ServiceNow tickets') {
            row 1
            col 2
            servicenowServer 'servicenow'
            detailsViewColumns 'number':'number','short_description':'short_description','state':'state','priority':'priority','assigned_to':'assigned_to.display_value'
          }
        }
      }
    }
    
  }
}
