package com.example.greeting;

import com.example.greeting.MyObject.Phone;
import com.example.greeting.MyObject.PhoneRepository;
import com.example.greeting.Service.PhoneService;
import com.example.greeting.Utils.CustomErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private PhoneRepository repository;

    private PhoneService phoneService = new PhoneService();

    @RequestMapping(value = "/greeting",method = RequestMethod.GET)
    public Greeting showGreeting(@RequestParam (name = "name", defaultValue = "World") String name){
        System.out.println("Greeting");
        return new Greeting(counter.incrementAndGet(),String.format(template,name));
    }

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public Greeting testTest(@RequestParam (name = "name", defaultValue = "World") String name){
        System.out.println("Test");
        repository.save(new Phone(counter.incrementAndGet(),"Oppo2","red","android"));
//        System.out.println(repository.findAll().get(0));

        return new Greeting(counter.incrementAndGet(),"Testing");
    }

    // Get all Phones
    @RequestMapping(value = "/phones",method = RequestMethod.GET)
    public List<Phone> getAllPhone(){
        System.out.println("Get all Phones");
//        System.out.println(repository.findAll());

//        return phoneService.getAllPhones();

        return repository.findAll();
    }

    // Get Single Phone
    @RequestMapping(value = "/phones/{id}",method = RequestMethod.GET)
    public ResponseEntity<?> getPhone(@PathVariable("id") long id){

//        Phone phone = phoneService.findById(id);
//        Phone phone = repository.findById(id).get();
        if(!repository.findById(id).isPresent()){
            System.out.println("can 't find" + id);
            return new ResponseEntity<>(new CustomErrorType("Phone with id " + id + " not found"),HttpStatus.NOT_FOUND);
        }
//        System.out.println("Get Phone with id " + repository.findById(id));

//        return new ResponseEntity<Phone>(phone,HttpStatus.OK);
        return new ResponseEntity<Phone>(repository.findById(id).get(),HttpStatus.OK);
    }


    // Create Phone
    @RequestMapping(value = "/phones/",method = RequestMethod.POST)
    public ResponseEntity<?> createPhone(@RequestBody Phone phone, UriComponentsBuilder ucBuilder){

        // Old version
//        System.out.println("Create new phone");
//
//        if(phoneService.isPhoneExist(phone)){
//            return new ResponseEntity<>(new CustomErrorType("Phone name : " +phone.getName() + " already exist "),HttpStatus.CONFLICT);
//        }
//        phoneService.addNewPhone(phone);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(ucBuilder.path("/phones/{id}").buildAndExpand(phone.getId()).toUri());
//
//        return new ResponseEntity<String>(headers,HttpStatus.CREATED);
//
//        // New version
        System.out.println("Create new phone");

        if(repository.findById(phone.id).isPresent()){
            return new ResponseEntity<>(new CustomErrorType("Phone name : " +phone.getName() + " already exist "),HttpStatus.CONFLICT);
        }

        repository.save(phone);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/phones/{id}").buildAndExpand(phone.getId()).toUri());

        return new ResponseEntity<String>(headers,HttpStatus.CREATED);



    }

    // Update Phone
    @RequestMapping(value = "/phones/{id}",method = RequestMethod.PUT)
    public ResponseEntity<?> updatePhones(@PathVariable("id") long id,@RequestBody Phone phone){
        System.out.println("Update phone with id");

        Phone curPhone = phoneService.findById(id);
        if(curPhone == null){
            return new ResponseEntity<>(new CustomErrorType("Can not find phone with id " + id),HttpStatus.NOT_FOUND);
        }


        curPhone.setName(phone.getName());
        curPhone.setDetailPhone(phone.getDetailPhone().getColor(),phone.getDetailPhone().getOs());

        phoneService.updatePhone(curPhone);

        return new ResponseEntity<Phone>(curPhone,HttpStatus.OK);
    }
    // Delete Phone
    @RequestMapping(value = "/phones/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePhone(@PathVariable("id") long id){

        // Old version
//        System.out.println("Delete phone with id");
//
//        Phone curPhone = phoneService.findById(id);
//        if(curPhone == null){
//            return new ResponseEntity<>(new CustomErrorType("Can not find phone with id " + id),HttpStatus.NOT_FOUND);
//        }
//
//        phoneService.deletePhoneWithId(id);
//        return new ResponseEntity<Phone>(HttpStatus.NO_CONTENT);

        //  New version
        System.out.println("Delete phone with id");

//        Phone curPhone = phoneService.findById(id);
        if(!repository.findById(id).isPresent()){
            return new ResponseEntity<>(new CustomErrorType("Can not find phone with id " + id),HttpStatus.NOT_FOUND);
        }

//        repository.delete(repository.findById(id).get());
        //New version
        repository.deleteById(id);
        return new ResponseEntity<Phone>(HttpStatus.NO_CONTENT);



    }


    // Delete All Phone
    @RequestMapping(value = "/phones/",method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAllPhone(){
        System.out.println("Delete all phones");

        // Old version
//        phoneService.deleteAll();

        //New version
        repository.deleteAll();
        return new ResponseEntity<Phone>(HttpStatus.NO_CONTENT);

    }
}
