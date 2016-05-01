package com.neva.javarel.app.adm.auth

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "adm_user")
class User {

    @Id
    @GeneratedValue
    @Column(name = "id")
    lateinit var id: Integer

    @Column(name = "name")
    lateinit var name: String

    @Column(name = "birth_date")
    lateinit var birthDate: Date

    constructor(name: String, birth: Date) {
        this.name = name
        this.birthDate = birth
    }

}