package com.swave.urnr.releasenote.service;

import com.swave.urnr.releasenote.domain.BlockContext;
import com.swave.urnr.releasenote.domain.NoteBlock;
import com.swave.urnr.releasenote.repository.BlockContextRepository;
import com.swave.urnr.releasenote.requestdto.BlockContextCreateRequestDTO;
import com.swave.urnr.releasenote.requestdto.BlockContextUpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableTransactionManagement
public class BlockContextServiceImpl implements BlockContextService {

    private final BlockContextRepository blockContextRepository;
    @Override
    @Transactional
    public BlockContext createBlockContext(BlockContextCreateRequestDTO blockContextCreateRequestDTO, NoteBlock noteBlock){
        BlockContext blockContext = BlockContext.builder()
                .context(blockContextCreateRequestDTO.getContext())
                .tag(blockContextCreateRequestDTO.getTag())
                .index(blockContextCreateRequestDTO.getIndex())
                .noteBlock(noteBlock)
                .build();

        blockContextRepository.saveAndFlush(blockContext);

        return blockContext;
    }

    //todo 현재 업데이트 방법은 전체를 지우고 다시 생성하는 방식임, 로그를 남기는 점은 좋지만 그 외에는 별로임 업데이트 방법을 고민해 볼것
    @Override
    @Transactional
    public BlockContext updateBlockContext(BlockContextUpdateRequestDTO blockContextUpdateRequestDTO, NoteBlock noteBlock){
        BlockContext blockContext = BlockContext.builder()
                .context(blockContextUpdateRequestDTO.getContext())
                .tag(blockContextUpdateRequestDTO.getTag())
                .index(blockContextUpdateRequestDTO.getIndex())
                .noteBlock(noteBlock)
                .build();

        blockContextRepository.saveAndFlush(blockContext);

        return blockContext;

    }

}
