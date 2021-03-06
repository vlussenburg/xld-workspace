#
# Copyright 2018 XEBIALABS
#
# Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
#

import sys
import time
import traceback
import urllib
from youtrack.connection import Connection as YouTrack

if server is None:
    sys.exit("No server provided.")

if query is None:
    sys.exit("No query provided.")

if fieldsToUpdate is None or len(fieldsToUpdate.items())== 0:
    sys.exit("No fields to update provided.")

if server['token']:
    # authentication request with permanent token
    yt = YouTrack(server['url'], token=server['token'])
else: 
    # versus authentication with username and password
    yt = YouTrack(server['url'], login=server['username'], password=server['password'])


command = ""
for key in fieldsToUpdate:
    command += "%s %s" % (key, fieldsToUpdate[key])

foundIssues = yt.getAllIssues(filter=urllib.urlencode(query), withFields=[])

for issue in foundIssues:
    yt.executeCommand(issue['id'], command, comment)
