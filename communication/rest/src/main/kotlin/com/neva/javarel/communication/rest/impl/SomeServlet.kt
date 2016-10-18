package com.neva.javarel.communication.rest.impl

import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Properties
import org.apache.felix.scr.annotations.Property
import org.apache.felix.scr.annotations.Service
import javax.servlet.Servlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service(Servlet::class)
@Component(immediate = true)
@Properties(
        Property(name = "alias", value = "/some")
)
class SomeServlet : HttpServlet() {

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        resp!!.writer.write("Hello World!")
        resp.writer.flush()
    }
}