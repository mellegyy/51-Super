package com.example.service;

import com.example.repository.MainRepository;
import org.springframework.stereotype.Service;

@Service
public abstract class MainService<T> {

    protected final MainRepository<T> repository;

    // Constructor to accept a repository
    public MainService(MainRepository<T> repository) {
        this.repository = repository;
    }
}
