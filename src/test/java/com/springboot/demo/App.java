package com.springboot.demo;

import com.springboot.demo.utils.JwtUtils;

public class App {
    public static void main(String[] args) {

        System.out.println(JwtUtils.getTenantFromToken("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTczMjc3NTU2MiwiZXhwIjoxNzMyODExNTYyLCJ0ZW5hbnQiOiJhZG1pbiJ9.Wj26sAmFWqVJZxjoNw5b7AAgoRRynKfxFYC_gqSBuIQ"));
    }
}
