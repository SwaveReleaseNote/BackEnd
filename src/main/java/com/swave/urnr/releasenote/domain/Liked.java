package com.swave.urnr.releasenote.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@Entity
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE liked SET is_deleted = true WHERE liked_id = ?")
@NoArgsConstructor
public class Liked {
    @Id
    @Column(name = "liked_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "isLiked")
    private Boolean isLiked;

    @OneToOne
    @JoinColumn(name = "seen_check_id")
    private SeenCheck seenCheck;

    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;

    @Builder
    public Liked(Boolean isLiked, SeenCheck seenCheck) {
        this.isLiked = isLiked;

        //다른 entity와의 연결
        this.seenCheck = seenCheck;
    }
}
