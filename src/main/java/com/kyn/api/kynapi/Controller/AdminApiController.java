package com.kyn.api.kynapi.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kyn.api.kynapi.Model.Store;
import com.kyn.api.kynapi.Services.StoreService;

@RestController
@RequestMapping("/kynapi/admin")
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminApiController {
    @Autowired
    StoreService storeService;

    @PatchMapping("/edit-store/{id}")
    public ResponseEntity<?> EditStore(@PathVariable("id") long id, @RequestBody Store store){
        Store currentRecord = storeService.findById(id);
        currentRecord.setLocations(store.getLocations());
        currentRecord.setContactNo(store.getContactNo());
        currentRecord.setStoreEmail(store.getStoreEmail());
        currentRecord.setStoreOwner(store.getStoreOwner());

        return new ResponseEntity<>(storeService.updateAndInsertStore(currentRecord), HttpStatus.OK);
    }

    @PostMapping("/add-store")
    public ResponseEntity<?> AddStore(@RequestBody Store store){
        return new ResponseEntity<>(storeService.updateAndInsertStore(store), HttpStatus.OK);
    }

    @DeleteMapping("/delete-store/{id}")
    public ResponseEntity<?> deleteStore(@PathVariable long id){
        storeService.deleteStore(id);
        return new ResponseEntity<>(storeService.allStores(), HttpStatus.ACCEPTED);
    }   

    @GetMapping("/get-store/{id}")
    public ResponseEntity<?> getStore(@PathVariable("id") long id){
        return new ResponseEntity<>(storeService.findById(id), HttpStatus.OK);
    }  

}
