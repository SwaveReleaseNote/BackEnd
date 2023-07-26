package com.swave.urnr.releasenote.service;

import com.swave.urnr.releasenote.domain.BlockContext;
import com.swave.urnr.releasenote.domain.NoteBlock;
import com.swave.urnr.releasenote.requestdto.BlockContextCreateRequestDTO;
import com.swave.urnr.releasenote.requestdto.BlockContextUpdateRequestDTO;

public interface BlockContextService {
    public BlockContext createBlockContext(BlockContextCreateRequestDTO blockContextCreateRequestDTO, NoteBlock noteBlock);
    public BlockContext updateBlockContext(BlockContextUpdateRequestDTO blockContextUpdateRequestDTO, NoteBlock noteBlock);
} 
