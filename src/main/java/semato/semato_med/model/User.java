package semato.semato_med.model;

import lombok.*;
import org.hibernate.annotations.NaturalId;
import semato.semato_med.model.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class User extends DateAudit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @NonNull
    @NaturalId
    @Column(unique = true)
    private String email;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    @NotBlank
    private String password;

    private String phone;

//    private LocalDateTime deletedAt;
//
//    private LocalDateTime updatedAt;
//
//    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Admin admin;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Patient patient;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Physician physician;

}