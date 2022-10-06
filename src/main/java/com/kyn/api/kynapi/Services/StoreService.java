package com.kyn.api.kynapi.Services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kyn.api.kynapi.Model.Store;
import com.kyn.api.kynapi.Repository.StoreRepository;

@Service
@Transactional
public class StoreService {

    @Autowired
    StoreRepository storeRepository;

    public List<Store> searchStores(String keyword) {
        if (keyword.equals("all")){
            return storeRepository.findAll();
        }else{
            return storeRepository.findByKeyword(keyword);
        }
    }
    public List<Store> allStores() {
        return storeRepository.findAll();
    }

    

    public Store updateAndInsertStore(Store store){
        return storeRepository.save(store);
    }

    public void deleteStore(long id){
        storeRepository.deleteByStoreId(id);
    }
    
    public Store findById(long id){
        return storeRepository.findById(id).orElseThrow(()-> new RuntimeException("no store found"));
    }
}
