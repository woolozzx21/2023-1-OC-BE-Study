package com.example.hellospring.controller;

import org.springframework.beans.factory.annotation.Autowired;

public class MemberForm {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
