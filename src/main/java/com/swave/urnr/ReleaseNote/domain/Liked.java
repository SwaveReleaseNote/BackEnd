package com.swave.urnr.ReleaseNote.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
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

    @Builder
    public Liked(Boolean isLiked, SeenCheck seenCheck) {
        this.isLiked = isLiked;

        //다른 entity와의 연결
        this.seenCheck = seenCheck;
    }
}
