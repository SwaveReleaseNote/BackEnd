package com.swave.urnr.releasenote.domain;

import com.swave.urnr.user.domain.UserInProject;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/*
* 개발자/관리자의 조회여부를 나타냄!
* 전체 (구독자 포함)의 조회수는 releaseNote의 count
*/
@Data
@Entity
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE seen_check SET is_deleted = true WHERE seen_check_id = ?")
@NoArgsConstructor
public class SeenCheck {
    @Id
    @Column(name = "seen_check_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seen_user")
    private String userName;

    @OneToOne(orphanRemoval = true )
    @JoinColumn(name = "release_note_id")
    private ReleaseNote releaseNote;

    @ManyToOne
    @JoinColumn(name = "user_in_project_id")
    private UserInProject userInProject;

    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;

    @Builder
    public SeenCheck(String userName, ReleaseNote releaseNote, UserInProject userInProject) {
        this.userName = userName;

        //다른 entity와의 연결
        this.releaseNote = releaseNote;
        this.userInProject = userInProject;
    }
}
