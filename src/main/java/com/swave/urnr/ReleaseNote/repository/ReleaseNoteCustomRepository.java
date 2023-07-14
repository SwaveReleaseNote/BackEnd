package com.swave.urnr.ReleaseNote.repository;

public interface ReleaseNoteCustomRepository {

    String latestReleseNote(Long userId,Long projectId);
}
