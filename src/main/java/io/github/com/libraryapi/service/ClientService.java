package io.github.com.libraryapi.service;

import io.github.com.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.com.libraryapi.model.Client;
import io.github.com.libraryapi.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repository;
    private final PasswordEncoder encoder;

    public Client salvar(Client client) {
        if (repository.existsByClientId(client.getClientId())) {
            throw new RegistroDuplicadoException("ClientId já cadastrado.");
        }

        client.setClientSecret(encoder.encode(client.getClientSecret()));
        return repository.save(client);
    }

    public Client obterPorClientId(String clientId) {
        return repository.findByClientId(clientId);
    }
}
