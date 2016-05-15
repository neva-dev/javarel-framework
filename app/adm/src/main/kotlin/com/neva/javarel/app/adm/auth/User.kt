package com.neva.javarel.app.adm.auth

import com.neva.javarel.security.auth.api.Authenticable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "adm_user")
open class User : Authenticable {

    companion object {
        const val EMAIL_COLUMN = "email"
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    lateinit var id: Integer

    @Column(name = EMAIL_COLUMN)
    lateinit var email: String

    @Column
    lateinit var password: String

    @Column
    lateinit var name: String

    @Column
    lateinit var birth: Date

    constructor(email: String, password: String, name: String, birth: Date) {
        this.email = email
        this.password = password
        this.name = name
        this.birth = birth
    }

    constructor() {
        // default constructor
    }

    override val authIdentifier: String
        get() = email
    override val authPassword: String
        get() = password

}