package dev.marinus.backend.service;

import dev.marinus.backend.dto.DnsDto;
import dev.marinus.backend.model.Dns;
import dev.marinus.backend.repository.DnsRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.remoting.dns.DnsResolver;
import org.springframework.security.remoting.dns.JndiDnsResolver;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@EnableScheduling
public class DnsService {

    private final DnsRepository repository;
    private final JndiDnsResolver dnsResolver = new JndiDnsResolver();

    public DnsService(DnsRepository repository) {
        this.repository = repository;
    }

    public Optional<DnsDto> resolveDomain(final String domain) {
        return repository.findByDomain(domain)
                .or(() -> resolveDomainFromDnsServer(domain))
                .map(dns -> new DnsDto(dns.getDomain(), dns.getIp()));
    }

    private Optional<Dns> resolveDomainFromDnsServer(final String domain) {
        try {
            final Optional<Dns> dns = Optional.ofNullable(this.dnsResolver.resolveIpAddress(domain))
                    .map(ip -> new Dns(domain, ip));
            dns.ifPresent(repository::save);
            return dns;
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Scheduled(fixedRate = 20, timeUnit = TimeUnit.MINUTES, initialDelay = 0)
    public void cleanUpOldDnsRecords() {
        final Date date = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1));
        repository.deleteAll(repository.findByResolvedAtBefore(date));
    }
}
