package com.neva.javarel.security.auth.api

import org.apache.shiro.web.mgt.WebSecurityManager

interface Auth {

    val security: WebSecurityManager

}