package dev.marinus.backend.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "guest_user")
public class GuestUser extends User {
    
    @Column(name = "save_in_local_storage")
    private boolean saveInLocalStorage;

}