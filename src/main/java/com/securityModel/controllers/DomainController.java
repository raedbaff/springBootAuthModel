package com.securityModel.controllers;

import com.securityModel.Services.DomainService;
import com.securityModel.models.DomainMedical;
import com.securityModel.repository.DomainRepository;
import com.securityModel.utils.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/domain")

public class DomainController {
    @Autowired
    DomainService ds;
    @Autowired
    StorageService storage;
    @Autowired
    DomainRepository domainRepository;
    @GetMapping("/all")
    public List<DomainMedical> getAllDomains(){

        return ds.GetAllDomains();

    }
    @PostMapping("/save")
    public void SaveDomain(DomainMedical dom, @RequestParam("file") MultipartFile file ) throws IOException {
        String filename=storage.store(file);
        dom.setPhoto(filename);

        ds.AddDomain(dom);
    }
    @DeleteMapping("/delete/{id}")
    public void  DeleteDomain(@PathVariable Long id) {
        ds.DeleteDomain(id);
    }
    @GetMapping("/{id}")
    public  DomainMedical getDomain(@PathVariable("id") Long id) {
        return ds.GetDomain(id);
    }



}
