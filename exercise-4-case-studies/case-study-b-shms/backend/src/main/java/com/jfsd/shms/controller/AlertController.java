package com.jfsd.shms.controller;

import com.jfsd.shms.dto.AlertDto;
import com.jfsd.shms.service.AlertService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    // GET /api/alerts?doctorId=1&open=true
    @GetMapping
    public List<AlertDto> search(@RequestParam(required = false) Long patientId,
                                 @RequestParam(required = false) Long doctorId,
                                 @RequestParam(defaultValue = "false") boolean open) {
        return alertService.search(patientId, doctorId, open);
    }

    @PostMapping("/{id}/acknowledge")
    public AlertDto acknowledge(@PathVariable Long id) {
        return alertService.acknowledge(id);
    }
}
