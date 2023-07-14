package com.swave.urnr.user.domain;

import com.swave.urnr.ReleaseNote.domain.Comment;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
//@Data
@NoArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id; // unsigend int
    @Column(name = "email")
    private String email; // @ 이메일 유효성 검사
    @Column(name = "password")
    private String password;
    @Column(name = "department")
    private String department;
    @Column(name = "user_name")
    private String name;
    @Column(name = "provider")
    private String provider;

    // user 와 userinPorject mapping
    @Column(name = "user_in_project_id")
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserInProject> userInProjectList;

    // user 와 comment mapping
    @Column(name = "comment_id")
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Comment> commentList;

    @Builder
    public User(String email, String password, String department, String name, String provider) {
        this.email = email;
        this.password = password;
//        this.department = department;
        this.name = name;
        this.provider=provider;
    }
}
