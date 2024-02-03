package dev.marinus.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "dns_data")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Dns {


    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "domain", unique = true)
    private String domain;
    @Column(name = "ip")
    private String ip;
    @Column(name = "resolved_at")
    private Date resolvedAt;

    public Dns(String domain, String ip) {
        this.domain = domain;
        this.ip = ip;
        this.resolvedAt = new Date();
    }
}
