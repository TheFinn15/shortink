package com.init.shortink.security.model

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "AUTHORITY")
class Authority(name: String) {

    @Id
    @Column(name = "NAME", length = 50, nullable = false)
    var name: String? = name

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val authority: com.init.shortink.security.model.Authority = o as com.init.shortink.security.model.Authority
        return name === authority.name
    }

    override fun hashCode(): Int {
        return Objects.hash(name)
    }

    override fun toString(): String {
        return "Authority{" +
                "name=" + name +
                '}'
    }
}