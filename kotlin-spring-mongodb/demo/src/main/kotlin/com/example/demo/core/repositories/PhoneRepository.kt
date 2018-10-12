package com.example.demo.core.repositories

import com.example.demo.api.request.Phone
import org.springframework.data.mongodb.repository.MongoRepository

interface PhoneRepository : MongoRepository<Phone,Long> {

}