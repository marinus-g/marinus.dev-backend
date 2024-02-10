package dev.marinus.backend.repository;

import dev.marinus.backend.model.Dns;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DnsRepository extends CrudRepository<Dns, Long> {

    Optional<Dns> findByDomain(String domain);

    List<Dns> findByResolvedAtBefore(Date date);

}
