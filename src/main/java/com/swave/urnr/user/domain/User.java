package com.swave.urnr.user.domain;

import com.swave.urnr.releasenote.domain.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE user SET is_deleted = true where user_id = ?")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id; // unsigend int
    @Column(name = "email")
    private String email; // @ 이메일 유효성 검사
    @Column(name = "password")
    private String password;
    @Column(name = "department")
    private String department;
    @Column(name = "username")
    private String username;
    @Column(name = "provider")
    private String provider;
    @Column(name= "loginState")
    private boolean isOnline=Boolean.FALSE;

    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;


    @Column(name = "user_in_project_id")
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserInProject> userInProjectList;

    // user 와 comment mapping
    @Column(name = "comment_id")
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,  orphanRemoval = true)
    private List<Comment> commentList;

    @Builder
    public User(String email, String password, String name, String provider) {
        this.email = email;
        this.password = password;
        this.username = name;
        this.provider = provider;
    }
}
