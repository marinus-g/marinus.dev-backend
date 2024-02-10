package dev.marinus.backend.controller;

import dev.marinus.backend.dto.DnsDto;
import dev.marinus.backend.service.DnsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/dns")
public class DnsController {

    private final DnsService dnsService;

    public DnsController(DnsService dnsService) {
        this.dnsService = dnsService;
    }

    @GetMapping( "/{domain}")
    public ResponseEntity<DnsDto> getDnsRecord(@PathVariable String domain) {
        return dnsService.resolveDomain(domain)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}