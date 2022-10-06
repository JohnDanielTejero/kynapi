package com.kyn.api.kynapi.Controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.aspectj.apache.bcel.classfile.Module.Uses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kyn.api.kynapi.Model.Store;
import com.kyn.api.kynapi.Model.User;
import com.kyn.api.kynapi.Model.Payload.UserProfile;
import com.kyn.api.kynapi.Security.UserPrincipal;
import com.kyn.api.kynapi.Services.StoreService;
import com.kyn.api.kynapi.Services.UserService;

@RestController
@RequestMapping("/kynapi/auth")
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("isAuthenticated()")
public class AuthApiController {

    @Autowired
    UserService userService;

    @Autowired
    StoreService storeService;

    @GetMapping("/store-listing")
    public List<Store> searchStores(@RequestParam("search") String keyword){
        return storeService.searchStores(keyword);
    }

    
    @GetMapping("/store-listing-all")
    public List<Store> allStores(){
        return storeService.allStores();
    }
    
    /**
     * @param userPrincipal
     * @return
     */
    @GetMapping("/current-user")
    public UserProfile profile(@AuthenticationPrincipal UserPrincipal userPrincipal){
        User user = userService.findById(userPrincipal.getId());
        return new UserProfile(user);
    }


    @GetMapping("/is-user-admin")
    public Boolean isAdmin(@AuthenticationPrincipal UserPrincipal userPrincipal){

        for (GrantedAuthority gAuthority: userPrincipal.getAuthorities()){
            if(gAuthority.getAuthority().equalsIgnoreCase("ROLE_ADMIN")){
                return true;
            }
        }
        return false;
    }

    @PatchMapping("/edit-user-profile")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal UserPrincipal userLogged, @RequestBody User user) throws IOException{

        User currentRecord = userService.findByEmail(userLogged.getEmail());

        boolean lnameIsValid = true;
        boolean fnameIsValid = true;
        boolean contactIsValid = true;

        //check for first name
        if (!user.getFirstName().isEmpty() && user.getFirstName() != null){
            if(userService.validateName(user.getFirstName())){
                currentRecord.setFirstName(user.getFirstName());
            }else{
                fnameIsValid = false;
            }
        }else{
            fnameIsValid = false;
        }

        //check for last name
        if (!user.getLastName().isEmpty() && user.getLastName() != null){
            if(userService.validateName(user.getLastName())){
                currentRecord.setLastName(user.getLastName());
            }else{
                lnameIsValid = false;
            }
        }else{
            lnameIsValid = false;
        }

        //check for birth date
        if (user.getBirthDate() != null){
            currentRecord.setBirthDate(user.getBirthDate());
        }


        //contact number
        if (!user.getContactNo().isEmpty() && user.getContactNo() != null){
            if (userService.validateContact(user.getContactNo())){
                currentRecord.setContactNo(user.getContactNo());
            }else{
                contactIsValid = false;
            }
        }

        if (contactIsValid && fnameIsValid && lnameIsValid){
            userService.updateAndInsertUser(currentRecord);
            //System.out.println(System.getProperty("user.dir") + "/src/main/resources/static/img");
        }

        
        return new ResponseEntity<>(new UserProfile(currentRecord), HttpStatus.ACCEPTED);
    }


    @PatchMapping("/edit-user-image")
    public ResponseEntity<?> updateProfileImage(@AuthenticationPrincipal UserPrincipal user, @RequestParam("image") MultipartFile file) throws IOException{

        User currentRecord = userService.findByEmail(user.getEmail());
        //check for image
        if (file != null && !file.isEmpty()) {
			
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            
            if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("jpeg")){
                
                String filename = System.getProperty("user.dir") + "/src/main/resources/static/img/" + file.getOriginalFilename();
                currentRecord.setImgPath("http://localhost:8080/img/" + file.getOriginalFilename());
                try {
                    file.transferTo(new File(filename));
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }

            }

		}
        userService.updateAndInsertUser(currentRecord);
        return new ResponseEntity<>(new UserProfile(currentRecord), HttpStatus.ACCEPTED);
    }

}
