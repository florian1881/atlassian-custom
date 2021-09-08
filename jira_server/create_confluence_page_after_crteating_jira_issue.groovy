/**
 This script doing:
 1. Create a Confluence page after creating a Jira issue
 2. Create dual link both in Jira issue and Confluence page
 3. Confluence page embeds Jira issue key and summary
 4. Confluence page has a simple template
*/
package examples.docs

import com.atlassian.applinks.api.ApplicationLink
import com.atlassian.applinks.api.ApplicationLinkService
import com.atlassian.applinks.api.application.confluence.ConfluenceApplicationType
import com.atlassian.jira.issue.Issue
import com.atlassian.sal.api.component.ComponentLocator
import com.atlassian.sal.api.net.Request
import com.atlassian.sal.api.net.Response
import com.atlassian.sal.api.net.ResponseException
import com.atlassian.sal.api.net.ResponseHandler
import groovy.json.JsonBuilder
import groovy.xml.MarkupBuilder


def ApplicationLink getPrimaryConfluenceLink() {
    def applicationLinkService = ComponentLocator.getComponent(ApplicationLinkService.class)
    final ApplicationLink conflLink = applicationLinkService.getPrimaryApplicationLink(ConfluenceApplicationType.class);
    conflLink
}

// the issue provided to us in the binding
Issue issue = issue

// if you don't want to create confluence pages based on some criterion like issue type, handle this, eg:
//if (!issue.getIssueType() == "Bug") {
//    return
//}

def confluenceLink = getPrimaryConfluenceLink()
assert confluenceLink // must have a working app link set up

def authenticatedRequestFactory = confluenceLink.createAuthenticatedRequestFactory()

// write storage format using an XML builder
def writer = new StringWriter()
def xml = new MarkupBuilder(writer)
//xml.p("关联的Jira Story")
//xml.'ac:structured-macro' ('ac:name': "jira") {
//    'ac:parameter' ('ac:name': "server", "JIRA")
//    'ac:parameter' ('ac:name': "serverId", "{UUID}") //replace the {UUID} to your Jira's
//    'ac:parameter' ('ac:name': "key", issue.key)
//}

// add more paragraphs etc
xml.'ac:layout' {
    'ac:layout-section'('ac:type':"single") {
        'ac:layout-cell' {
            p '关联的Jira Story'
            p {
                'ac:structured-macro' ('ac:name': "jira") {
                'ac:parameter' ('ac:name': "server", "JIRA")
                'ac:parameter' ('ac:name': "serverId", "{UUID}") //replace the {UUID} to your Jira's
                'ac:parameter' ('ac:name': "key", issue.key)
                }
            }
        }
    }
    'ac:layout-section'('ac:type':"single") {
        'ac:layout-cell' {
            h1 '1. Story'
            p {
                'ac:structured-macro' ('ac:name': "jira") {
                'ac:parameter' ('ac:name': "columns", "summary,description")
                'ac:parameter' ('ac:name': "server", "JIRA")
                'ac:parameter' ('ac:name': "serverId", "{UUID}") //replace the {UUID} to your Jira's
                'ac:parameter' ('ac:name': "jqlQuery", "key = " + issue.key)
                'ac.parameter' ('ac:name': "maximumIssues", 1)
                }
            }
        }
    }
    'ac:layout-section'('ac:type':"single") {
        'ac:layout-cell' {
            h1 '2. 技术方案'
            p ''
            p ''
        }
    }
    'ac:layout-section'('ac:type':"single") {
        'ac:layout-cell' {
            h1 '3. 功能设计'
            p ''
            p ''
        }
    }
}

// print the storage that will be the content of the page
log.debug(writer.toString())

// set the page title - this should be unique in the space or page creation will fail
def pageTitle = issue.key +  " " +issue.summary + "-[关联文档]"

def params = [
    type: "page",
    title: pageTitle,
    space: [
        key: "{space key}" // set the space key - or calculate it from the project or something like issue.getProjectObject().getKey()
    ],
    /* // if you want to specify create the page under another, do it like this:
	*/
		ancestors: [
         [
             type: "page",
             id: "{page id}", //replace {page id} to an actual page id
         ]    
    ],
    body: [
        storage: [
            value: writer.toString(), 
            representation: "storage" 
        ]
    ]
]

authenticatedRequestFactory
    .createRequest(Request.MethodType.POST, "rest/api/content")
    .addHeader("Content-Type", "application/json")
    .setRequestBody(new JsonBuilder(params).toString())
    .execute(new ResponseHandler<Response>() {
    @Override
    void handle(Response response) throws ResponseException {
        if(response.statusCode != HttpURLConnection.HTTP_OK) {
            throw new Exception(response.getResponseBodyAsString())
        }
    }
})

