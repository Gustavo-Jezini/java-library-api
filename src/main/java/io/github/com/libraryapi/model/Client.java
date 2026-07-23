package io.github.com.libraryapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "client", schema = "public")
@Getter
@Setter
@ToString
public class Client {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "client_id", length = 150, nullable = false)
    private String clientId;

    @Column(name = "client_secret", length = 400, nullable = false)
    private String clientSecret;

    @Column(name = "redirect_uri", length = 200, nullable = false)
    private String redirectUri;

    @Column(name = "scope", length = 50)
    private String scope;
}
