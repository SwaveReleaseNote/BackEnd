package com.swave.urnr.releasenote.service;

import com.swave.urnr.releasenote.domain.BlockContext;
import com.swave.urnr.releasenote.domain.NoteBlock;
import com.swave.urnr.releasenote.domain.ReleaseNote;
import com.swave.urnr.releasenote.domain.SeenCheck;
import com.swave.urnr.releasenote.requestdto.BlockContextCreateRequestDTO;
import com.swave.urnr.releasenote.requestdto.BlockContextUpdateRequestDTO;
import com.swave.urnr.user.domain.UserInProject;

public interface SeenCheckService {
    SeenCheck createSeenCheck(String username , ReleaseNote releaseNote, UserInProject userInProject);
} 
