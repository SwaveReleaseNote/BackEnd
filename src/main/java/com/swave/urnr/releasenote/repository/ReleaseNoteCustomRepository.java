package com.swave.urnr.releasenote.repository;

import com.swave.urnr.releasenote.domain.ReleaseNote;
import com.swave.urnr.user.domain.UserInProject;

public interface ReleaseNoteCustomRepository {

    ReleaseNote findMostRecentReleaseNote(Long userId);

    UserInProject findUserInProjectByUserIdAndReleaseNoteId(Long userId, Long releaseNote_id);

    String latestReleseNote(Long projectId);

}
