#
# Copyright (c) 2018. All rights reserved.
#
# This software and all trademarks, trade names, and logos included herein are the property of XebiaLabs, Inc. and its affiliates, subsidiaries, and licensors.
#

# Remove old web content if it's still there
if (Test-Path $deployed.targetPath) {
	Write-Host "Removing old web content from [$($deployed.targetPath)]."
	Get-ChildItem -Path $deployed.targetPath -Recurse | Remove-Item -Force -Recurse | Out-Null
	Remove-Item $deployed.targetPath -Force | Out-Null
}

# Copy new web content
Write-Host "Copying web content to [$($deployed.targetPath)]."
Copy-Item -Recurse -Force $deployed.file $deployed.targetPath

$source = Get-ChildItem -Recurse -path $deployed.file
$target = Get-ChildItem -Recurse -path $deployed.targetPath

Compare-Object -ReferenceObject $source -DifferenceObject $target
