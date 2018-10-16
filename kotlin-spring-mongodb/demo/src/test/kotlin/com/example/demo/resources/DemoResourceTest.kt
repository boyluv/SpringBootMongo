package com.example.demo.resources

import com.example.demo.api.request.Phone
import com.example.demo.core.services.PhoneService
import org.hamcrest.Matchers.`is`
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup
import org.springframework.web.client.RestTemplate
import java.util.ArrayList
import com.fasterxml.jackson.databind.ObjectMapper




@RunWith(SpringRunner::class)
@SpringBootTest
class DemoResourceTest {
    private lateinit var mvc: MockMvc

    @Autowired
    lateinit var cotroller: DemoResource

    @MockBean
    lateinit var phoneService: PhoneService

    private var testPhone: Phone? = null

    private var testListPhone:ArrayList<Phone>? = ArrayList<Phone>()

    @Before
    fun setUp() {
        this.mvc = standaloneSetup(this.cotroller).build()// Standalone context
        testPhone = Phone(1, "Apple", Phone.DetailPhone("Black", "Android"))

        testListPhone?.add(Phone(1, "Apple", Phone.DetailPhone("Black", "Android")))
        testListPhone?.add(Phone(2, "Oppo", Phone.DetailPhone("White", "Android")))

    }

    @Test
    fun getAllPhone() {
        `when`(phoneService.getAllPhone()).thenReturn(testListPhone)
        mvc.perform(get("/phones").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath<String>("$[0].name", `is`<String>("Apple")))
                .andExpect(jsonPath<String>("$[1].name", `is`<String>("Oppo")))
    }

    @Test
    fun getPhone() {
        `when`(phoneService.getPhoneWithId(1)).thenReturn(testPhone)

        mvc.perform(get("/phones/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath<String>("$.name", `is`<String>("Apple")))
                .andExpect(jsonPath<String>("$.detailPhone.color", `is`<String>("Black")))
                .andExpect(jsonPath<String>("$.detailPhone.os", `is`<String>("Android")))
    }

    @Test
    fun getPhoneIdNotExist() {
        `when`(phoneService.getPhoneWithId(2)).thenReturn(null)

        mvc.perform(get("/phones/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound)

    }

    @Test
    fun createPhone() {
        val mapper = ObjectMapper()
        val requestJson = mapper.writeValueAsString(testPhone)
        this.mvc.perform(post("/phones/")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated)
    }

    @Test
    fun updatePhones() {

    }

    @Test
    fun deletePhone() {
    }

    @Test
    fun deleteAllPhone() {
    }

    @Test
    fun getResponseEntity() {
    }

    @Test
    fun `getCounter$production_sources_for_module_demo`() {
    }
}