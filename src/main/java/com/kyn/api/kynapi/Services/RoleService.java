package com.kyn.api.kynapi.Services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.kyn.api.kynapi.Model.Roles;
import com.kyn.api.kynapi.Repository.RoleRepository;

@Component
public class RoleService implements Converter<String, Roles>{

    @Autowired
    RoleRepository repository;

    @Override
    public Roles convert(String source) {
        return repository.findByRoleName(source).get();
    }
    
}
