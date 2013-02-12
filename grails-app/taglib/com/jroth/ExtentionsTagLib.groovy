package com.jroth

import org.codehaus.groovy.grails.web.pages.FastStringWriter
import org.codehaus.groovy.grails.web.sitemesh.GSPSitemeshPage
import org.codehaus.groovy.grails.web.util.GrailsPrintWriter
import org.codehaus.groovy.grails.web.util.StreamCharBuffer

class ExtentionsTagLib {
    static namespace = 'ext'

    /**
     * ext:addContent
     * Requires 'pageProperty' be set to an existing named content area.
     */
    def addContent = { attrs, content ->
        if (!attrs.pageProperty) {
            throwTagError("Tag [addContent] requires attribute [pageProperty]")
        }

        def htmlPage = request[org.codehaus.groovy.grails.web.sitemesh.GrailsPageFilter.GSP_SITEMESH_PAGE]
        if (!(htmlPage instanceof GSPSitemeshPage)) {
            throwTagError("Tag [addContent] requires GSPSitemeshPage")
        }

        def contentBuffer = htmlPage.getContentBuffer("page.${attrs.pageProperty.toString()}")

        if (contentBuffer == null) {
            contentBuffer = wrapContentInBuffer(content)
            htmlPage.setContentBuffer(attrs.pageProperty.toString(), contentBuffer)
        } else {
            new GrailsPrintWriter(contentBuffer.writer) << content()
        }
    }

    /**
     * @param content the content in various forms (closure, string)
     * @return the content wrapped in a content buffer
     */
    def wrapContentInBuffer(content) {
        if (content instanceof Closure) {
            content = content()
        }
        if (!(content instanceof StreamCharBuffer)) {
            // the content closure might be a string constant, so wrap it in a StreamCharBuffer in that case
            def newBuffer = new FastStringWriter()
            newBuffer.print(content)
            content = newBuffer.buffer
        }
        return content
    }
}
