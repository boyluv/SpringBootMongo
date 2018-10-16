package com.example.demo.core.services

import com.example.demo.api.request.Phone

interface PhoneServiceI {
    fun getAllPhone() : List<Phone>?

    fun getPhoneWithId(id: Long) : Phone?

    fun createNewPhone(phone: Phone)

    fun updatePhone(curPhone: Phone,phone: Phone) : Phone?

    fun deletePhoneWithId(id: Long) : Boolean

    fun deleteAll()
}