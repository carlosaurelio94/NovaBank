package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ClientServiceImplement implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    public List<ClientDTO> getClientsDTO() {
        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
    }

    public ClientDTO getClientDTO(Long id){
       return clientRepository.findById(id).map(client -> new ClientDTO(client)).orElse(null);
    }

    public Client findByEmail(String email){
        return clientRepository.findByEmail(email);
    }

    public void saveClient(Client client) {
        clientRepository.save(client);
    }
}
