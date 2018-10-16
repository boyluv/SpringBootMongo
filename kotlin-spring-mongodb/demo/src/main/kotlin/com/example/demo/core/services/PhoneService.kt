package com.example.demo.core.services

import com.example.demo.api.request.Phone
import com.example.demo.core.repositories.PhoneRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicLong

@Service
class PhoneService @Autowired
constructor(
        val responseEntity : PhoneRepository,
        internal val counter : AtomicLong = AtomicLong()
) : PhoneServiceI{
    override fun getAllPhone(): List<Phone>? {
        if (responseEntity.findAll().isEmpty())
            return null
        return responseEntity.findAll()
    }

    override fun getPhoneWithId(id: Long): Phone? {
        if(!responseEntity.findById(id).isPresent())
            return null
        else
            return responseEntity.findById(id).get()
    }

    override fun createNewPhone(phone: Phone) {

        phone.id = counter.incrementAndGet()
        responseEntity.save(phone)
    }

    override fun updatePhone(curPhone: Phone,phone: Phone): Phone? {

        curPhone.name = phone.name
        curPhone.detailPhone.color = phone.detailPhone.color
        curPhone.detailPhone.os = phone.detailPhone.os

        responseEntity.save(curPhone)
        return curPhone
    }

    override fun deletePhoneWithId(id: Long): Boolean {
        if (!responseEntity.findById(id).isPresent()) {
            return false
        }
        responseEntity.deleteById(id)
        return true
    }

    override fun deleteAll() {
        responseEntity.deleteAll()
    }

}