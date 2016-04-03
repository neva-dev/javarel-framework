package com.neva.javarel.app.adm.auth

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "adm_user")
class User : Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private var id: Integer? = null;

    @Column(name = "name")
    private var name: String? = null

    fun getId(): Integer? {
        return id
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getName(): String? {
        return name
    }

}