package com.jm.security.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("denyAll()")
public class HelloWorldController {

    @GetMapping("/hellosec")
    @PreAuthorize("hasAuthority('READ')")
    public String secHelloWorld(){
        return "Hello World With Security";
    }

    @GetMapping("/hellonoseg")
    @PreAuthorize("permitAll()")
    public String noSecHelloWorld(){
        return "Hello World Without Security";
    }
}
