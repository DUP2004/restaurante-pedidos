package com.restaurante.pedidos.service;

import com.restaurante.pedidos.entity.Cliente;
import com.restaurante.pedidos.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClienteService {
    private final ClienteRepository repository;
    public ClienteService(ClienteRepository repository) { this.repository = repository; }

    public List<Cliente> listar() { return repository.findAll(); }
    public Cliente obtener(Long id) { return repository.findById(id).orElseThrow(() -> new RuntimeException("Cliente no encontrado")); }
    public Cliente guardar(Cliente cliente) { return repository.save(cliente); }
    public Cliente actualizar(Long id, Cliente data) {
        Cliente c = obtener(id);
        c.setNombre(data.getNombre());
        c.setApellido(data.getApellido());
        c.setCorreo(data.getCorreo());
        c.setTelefono(data.getTelefono());
        return repository.save(c);
    }
    public void eliminar(Long id) { repository.delete(obtener(id)); }
}
