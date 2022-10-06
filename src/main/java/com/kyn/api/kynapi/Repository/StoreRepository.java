package com.kyn.api.kynapi.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kyn.api.kynapi.Model.Store;

public interface StoreRepository extends JpaRepository<Store, Long>{

    @Query("select s from Store s where s.storeEmail LIKE %:k% or s.storeOwner LIKE %:k% or s.contactNo LIKE %:k% or s.locations LIKE %:k% or s.storeName LIKE %:k%")
    public List<Store> findByKeyword(@Param("k") String keyword);

    public void deleteByStoreId(long id);
    
}
