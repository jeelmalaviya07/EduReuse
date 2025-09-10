package com.project.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Entity
@Table(
        name = "users", // avoid reserved word 'user'
        indexes = {
                @Index(name = "idx_deleted_user", columnList = "deleted")
        }
)
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE user_id = ?")
@Where(clause = "deleted = false")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String fullName;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String mobile;

    @Enumerated(EnumType.STRING)
    private Role role; // USER, ADMIN

    private String city;

    private String state;

    private Timestamp createdAt;

    @Column(nullable = false)
    private boolean deleted = false; // soft delete flag

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    private List<BookSet> bookSets;

    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    private List<SwapRequest> swapRequests;
}
