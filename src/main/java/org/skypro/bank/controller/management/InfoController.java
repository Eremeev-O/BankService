package org.skypro.bank.controller.management;

import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management")
public class InfoController {

    private final BuildProperties buildProperties;

    public InfoController(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @GetMapping("/info")
    public InfoResponse getInfo() {
        return new InfoResponse(buildProperties.getName(), buildProperties.getVersion());
    }

    public record InfoResponse(String name, String version) {
    }
}
