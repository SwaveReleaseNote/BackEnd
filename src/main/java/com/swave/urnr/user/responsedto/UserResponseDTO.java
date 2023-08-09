package com.swave.urnr.user.responsedto;

import com.swave.urnr.releasenote.domain.Comment;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;


@ApiModel(value = "사용자 정보 반환용 DTO")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserResponseDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id; // unsigend int
    @Column(name = "email")
    private String email; // @ 이메일 유효성 검사
    @Column(name = "department")
    private String department;
    @Column(name = "username")
    private String username;




}
