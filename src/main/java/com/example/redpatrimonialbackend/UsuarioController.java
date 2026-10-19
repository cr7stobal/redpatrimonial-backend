package com.example.redpatrimonialbackend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository repo;

    public UsuarioController(UsuarioRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/registro")
    public ResponseEntity<Void> registro(@RequestBody Usuario u) {
        if (repo.findByMail(u.getMail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        u.setId(null);   // <- esta línea
        repo.save(u);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody Usuario u) {
        boolean valido = repo.findByMail(u.getMail())
                .map(guardado -> guardado.getPass().equals(u.getPass()))
                .orElse(false);
        return valido
                ? ResponseEntity.ok().build()                             // 200
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
    }
}