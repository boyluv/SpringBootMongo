package com.example.demo.resources

import com.example.demo.api.request.Phone
import com.example.demo.core.repositories.PhoneRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriBuilder
import org.springframework.web.util.UriComponentsBuilder
import java.util.concurrent.atomic.AtomicLong

@RestController
class DemoResource @Autowired
 constructor(
        val responseEntity : PhoneRepository,
        internal val counter : AtomicLong = AtomicLong()
) {
    @GetMapping("/test")
    fun helloWorld(): ResponseEntity<String>{
        println("Get all Phones")
        println(responseEntity.findAll().size)
        return ResponseEntity("Nice",HttpStatus.OK);
    }


    // Get all Phones
    @GetMapping("/phones")
    fun getAllPhone(): ResponseEntity<*>  {
        println("Get all Phones")
        if (responseEntity.findAll().isEmpty())
            return ResponseEntity<Any>(HttpStatus.NO_CONTENT)
        return ResponseEntity.ok(responseEntity.findAll())
    }

    // Get Single Phone
    @GetMapping("/phones/{id}")
    fun getPhone(@PathVariable("id") id: Long): ResponseEntity<*> {
        if (!responseEntity.findById(id).isPresent()) {
            println("can 't find $id")
            return ResponseEntity<Any>("Phone with id $id not found", HttpStatus.NOT_FOUND)
        }
        return  ResponseEntity.ok(responseEntity.findById(id).get())
    }

    // Create Phone
    @PostMapping("/phones/")
    fun createPhone(@RequestBody phone: Phone, ucBuilder: UriComponentsBuilder): ResponseEntity<*> {
        println("Create new phone")

        phone.id = counter.incrementAndGet()
        responseEntity.save(phone)
        val headers = HttpHeaders()
        headers.location = ucBuilder.path("/phones/{id}").buildAndExpand(  phone.id).toUri()

        return ResponseEntity<String>(headers, HttpStatus.CREATED)
    }

    // Update Phone
    @PutMapping("/phones/{id}")
    fun updatePhones(@PathVariable("id") id: Long, @RequestBody phone: Phone): ResponseEntity<*> {
        println("Update phone with id")

        if (!responseEntity.findById(phone.id).isPresent()) {
            return ResponseEntity<Any>(("Can not find phone with id $id"), HttpStatus.NOT_FOUND)
        }

        val curPhone = responseEntity.findById(id).get()

        curPhone.name = phone.name
        curPhone.detailPhone.color = phone.detailPhone.color
        curPhone.detailPhone.os = phone.detailPhone.os

        responseEntity.save(curPhone)

        return ResponseEntity(curPhone, HttpStatus.OK)

    }

    // Delete Phone
    @DeleteMapping("/phones/{id}")
    fun deletePhone(@PathVariable("id") id: Long): ResponseEntity<*> {
        println("Delete phone with id")

        if (!responseEntity.findById(id).isPresent()) {
            return ResponseEntity<Any>("Can not find phone with id $id", HttpStatus.NOT_FOUND)
        }
        responseEntity.deleteById(id)
        return ResponseEntity<Any>(HttpStatus.NO_CONTENT)
    }

    // Delete All Phone
    @DeleteMapping("/phones/")
    fun deleteAllPhone(): ResponseEntity<*> {
        println("Delete all phones")
        responseEntity.deleteAll()
        return ResponseEntity<Phone>(HttpStatus.NO_CONTENT)
    }

}