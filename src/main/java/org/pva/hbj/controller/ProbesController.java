package org.pva.hbj.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ProbesController {

    @GetMapping(value = "/health")
    public String health() {
        return "Health: ok";
    }
}
