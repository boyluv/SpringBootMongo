package com.example.demo.resources

import com.example.demo.api.request.Phone
import com.example.demo.core.repositories.PhoneRepository
import com.example.demo.core.services.PhoneService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriBuilder
import org.springframework.web.util.UriComponentsBuilder
import java.util.ArrayList
import java.util.concurrent.atomic.AtomicLong

@RestController
class DemoResource (private val phoneService: PhoneService ) {
    @GetMapping("/test")
    fun helloWorld(): ResponseEntity<String>{
        println("Get all Phones")
        println(phoneService.getAllPhone())
        return ResponseEntity("Nice",HttpStatus.OK);
    }

    // Get all Phones
    @GetMapping("/phones")
    fun getAllPhone(): ResponseEntity<*>  {
        println("Get all Phones")
        if (phoneService.getAllPhone() == null)
            return ResponseEntity<Any>(HttpStatus.NO_CONTENT)
        return ResponseEntity.ok(phoneService.getAllPhone().orEmpty())
    }

    // Get Single Phone
    @GetMapping("/phones/{id}")
    fun getPhone(@PathVariable("id") id: Long): ResponseEntity<*> {
        println("Get phone with id $id")

        val curPhone = phoneService.getPhoneWithId(id = id)
        if(curPhone == null){
            println("can 't find $id")
            return ResponseEntity<Any>("Phone with id $id not found", HttpStatus.NOT_FOUND)
        }
        return  ResponseEntity.ok(curPhone)
    }

    // Create Phone
    @PostMapping("/phones/")
    fun createPhone(@RequestBody phone: Phone, ucBuilder: UriComponentsBuilder): ResponseEntity<*> {
        println("Create new phone")

        phoneService.createNewPhone(phone)

        val headers = HttpHeaders()
        headers.location = ucBuilder.path("/phones/{id}").buildAndExpand(  phone.id).toUri()

        return ResponseEntity<String>(headers, HttpStatus.CREATED)

    }

//    // Update Phone
    @PutMapping("/phones/{id}")
    fun updatePhones(@PathVariable("id") id: Long, @RequestBody phone: Phone): ResponseEntity<*> {
        println("Update phone with id")

        val curPhone = phoneService.getPhoneWithId(id)

        if (curPhone == null) {
            return ResponseEntity<Any>(("Can not find phone with id $id"), HttpStatus.NOT_FOUND)
        }


        return ResponseEntity(phoneService.updatePhone(curPhone, phone), HttpStatus.OK)

    }

    // Delete Phone
    @DeleteMapping("/phones/{id}")
    fun deletePhone(@PathVariable("id") id: Long): ResponseEntity<*> {
        println("Delete phone with id")

        if (!phoneService.deletePhoneWithId(id)) {
            return ResponseEntity<Any>("Can not find phone with id $id", HttpStatus.NOT_FOUND)
        }
        return ResponseEntity<Any>(HttpStatus.NO_CONTENT)


    }

    // Delete All Phone
    @DeleteMapping("/phones/")
    fun deleteAllPhone(): ResponseEntity<*> {
        println("Delete all phones")
        phoneService.deleteAll()
        return ResponseEntity<Phone>(HttpStatus.NO_CONTENT)
    }

    // Get all Phones from anotherserver
    @GetMapping("/another/phones")
    fun getAllAnotherPhone(): List<Phone> {
        println("Get all Phones")
        val phoneList = ArrayList<Phone>()

        phoneList.add(Phone(4, "Oppo", Phone.DetailPhone("red", "android")))
        phoneList.add(Phone(5, "Nokia", Phone.DetailPhone("green", "window")))
        phoneList.add(Phone(6, "Apple", Phone.DetailPhone("blue", "ios")))

        return phoneList
    }
}