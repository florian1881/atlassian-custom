import com.atlassian.applinks.api.ApplicationLink
import com.atlassian.applinks.api.ApplicationLinkService
import com.atlassian.applinks.api.application.confluence.ConfluenceApplicationType
import com.atlassian.confluence.event.events.content.page.PageEvent
import com.atlassian.confluence.pages.Page
import com.atlassian.confluence.pages.AttachmentManager
import com.atlassian.confluence.pages.AttachmentUtils
import com.atlassian.confluence.pages.PageManager
import com.atlassian.sal.api.component.ComponentLocator
import com.atlassian.confluence.spaces.SpaceManager
import org.apache.log4j.Level
import org.apache.log4j.Logger

import com.atlassian.sal.api.net.Request
import com.atlassian.sal.api.net.Response
import com.atlassian.sal.api.net.ResponseException
import com.atlassian.sal.api.net.ResponseHandler
import com.atlassian.sal.api.net.RequestFilePart
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

import com.atlassian.spring.container.ContainerManager
import com.atlassian.confluence.pages.Attachment
import java.io.InputStream
import java.io.IOException
import com.atlassian.confluence.pages.persistence.dao.FileSystemAttachmentDataDao


log = Logger.getLogger("com.test.InlineScript")
log.setLevel(Level.DEBUG)

def ApplicationLink getPrimaryConfluenceLink() {
    def applicationLinkService = ComponentLocator.getComponent(ApplicationLinkService.class)
    final ApplicationLink conflLink = applicationLinkService.getPrimaryApplicationLink(ConfluenceApplicationType.class)
    conflLink
}

def event = event as PageEvent
def page = event.getPage()

def confluenceLink = getPrimaryConfluenceLink()
assert confluenceLink // Должен быть устанвлен линк для связи с други конфлуенсом
def authenticatedRequestFactory = confluenceLink.createImpersonatingAuthenticatedRequestFactory()


def spaceManager = ComponentLocator.getComponent(SpaceManager)
if ( page.getSpaceKey().equals("TEST")){

    log.debug("Получаем родителя и заголовок страницы")

    def parent = page.getParent().getTitle()
    def pageTitle = page.getTitle()
    def body = page.getBodyAsString()
    def space = page.getSpaceKey()
    def pageId = page.getId()
    def attach = page.getAttachments()

    //AttachmentManager attachmentManager = (AttachmentManager)ContainerManager.getComponent("attachmentManager")
    //Attachment attachment = attachmentManager.getAttachment(attachmentId)
    //def attachmentManager = (AttachmentManager) ContainerManager.getComponent("attachmentManager")
    //def attachment = attachmentManager.getAttachment(attachmentId)


    log.debug("Название спейса:" + space)
    log.debug("Название родительской страницы:" + parent)
    log.debug("Заголовок новой страницы:" + pageTitle)
    log.debug("Контент страницы:" + body)
    log.debug("ID текущей страницы:" + pageId)
    log.debug("Вложение:" + attach)
    log.debug("ID вложения/й:" + attach.id)
    log.debug("Имя вложения/й:" + attach.title)
    //log.debug("Имя вложения:" + attachment)
    //log.debug("Путь вложения:" + File)
    //log.debug("Путь где лежит аттач:" + getAttachmentDownloadPath( ,attach.first().title.toString()))



    //Отправлем запрос на получение ID родительской странцы в удаленной вики.
    def confResponse = null
    authenticatedRequestFactory
            .createRequest(Request.MethodType.GET, "rest/api/content" )
            .addHeader("Content-Type", "application/json")
            .execute(new ResponseHandler<Response>() {
        @Override
        void handle(Response response) throws ResponseException {
            confResponse = new JsonSlurper().parseText(response.getResponseBodyAsString())
        }
    })
    log.debug("Контент удаленной страницы :" + confResponse.toString())

    def id = null

    confResponse.results.each({
        if (it.title == parent) {
            id = it.id
        }
    })

    log.debug("ID родителя удаленной страницы :" + id)

    def params = [
            type: "page",
            title: pageTitle,
            space: [
                    key: "MAIN"
            ],


            ancestors: [
                    [
                            type: "page",
                            id: id,
                    ]
            ],

            body: [
                    storage: [
                            value: body,
                            representation: "storage"
                    ]
            ]
    ]

    log.debug("Блок авторизации и копирования новой страницы в другую вики, без вложения")

    def conf = null
    authenticatedRequestFactory
            .createRequest(Request.MethodType.POST, "rest/api/content")
            .addHeader("Content-Type", "application/json")
            .setRequestBody(new JsonBuilder(params).toString())
            .execute(new ResponseHandler<Response>() {
        @Override
        void handle(Response response) throws ResponseException {
            if(response.statusCode != HttpURLConnection.HTTP_OK) {
                throw new Exception(response.getResponseBodyAsString())
            } else {
                def webUrl = new JsonSlurper().parseText(response.responseBodyAsString)["_links"]["webui"]
                conf = new JsonSlurper().parseText(response.responseBodyAsString)
                log.debug("Что то получаем после создания станицы:" + conf)
            }
        }
    })

    log.debug("ID удаленной созданной страницы:" + conf.id)
    log.debug("Блок авторизации и копирования в новую страницу в другой вики, вложения")

    def path = AttachmentUtils.getConfluenceAttachmentDirectory()
    log.debug("папка куда складываются аттачи:" + path)

    def att = RequestFilePart.getPath()
    log.debug("папка 2:" + att)

    def filePath = null
    //RequestFilePart requestFilePart = new RequestFilePart("text/xml", attach.title, attach.id, "file")
    RequestFilePart requestFilePart = new RequestFilePart("application/octet-stream", attach.title, filePath, "file")
    List<RequestFilePart> fileParts = new ArrayList<RequestFilePart>()
    fileParts.add(requestFilePart)

    authenticatedRequestFactory
            .createRequest(Request.MethodType.POST, "rest/api/content/" + conf.id + "/child/attachment")
            .addHeader("X-Atlassian-Token", "nocheck")
            .setFiles(fileParts)
            .execute(new ResponseHandler<Response>() {
        @Override
        void handle(Response response) throws ResponseException {
            if(response.statusCode != HttpURLConnection.HTTP_OK) {
                throw new Exception(response.getResponseBodyAsString())
            } else {
                def webUrl = new JsonSlurper().parseText(response.responseBodyAsString)["_links"]["webui"]
            }
        }
    })




} else {
    log.debug("Создание страницы было не в нужном спейсе")
}
