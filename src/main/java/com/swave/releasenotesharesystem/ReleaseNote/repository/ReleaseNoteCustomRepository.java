package com.swave.releasenotesharesystem.ReleaseNote.repository;

public interface ReleaseNoteCustomRepository {

    String latestReleseNote(Long userId,Long projectId);
}
