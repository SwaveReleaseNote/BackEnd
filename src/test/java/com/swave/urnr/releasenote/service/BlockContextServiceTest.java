package com.swave.urnr.releasenote.service;

import com.swave.urnr.releasenote.domain.BlockContext;
import com.swave.urnr.releasenote.domain.NoteBlock;
import com.swave.urnr.releasenote.repository.BlockContextRepository;
import com.swave.urnr.releasenote.repository.NoteBlockRepository;
import com.swave.urnr.releasenote.repository.ReleaseNoteRepository;
import com.swave.urnr.releasenote.requestdto.BlockContextCreateRequestDTO;
import com.swave.urnr.releasenote.requestdto.BlockContextUpdateRequestDTO;
import com.swave.urnr.util.querydsl.QueryDslConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(QueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BlockContextServiceTest {

    @Autowired
    private BlockContextService blockContextService;
    @Autowired
    private BlockContextRepository blockContextRepository;
    @Autowired
    private NoteBlockRepository noteBlockRepository;

    @Test
    @DisplayName("블럭 컨텍스트 생성 테스트")
    @Transactional
    void createBlockContext() {
        BlockContextCreateRequestDTO blockContextCreateRequestDTO = new BlockContextCreateRequestDTO();
        blockContextCreateRequestDTO.setContext("test");
        blockContextCreateRequestDTO.setTag("H1");
        blockContextCreateRequestDTO.setIndex(1L);

        NoteBlock noteBlock = new NoteBlock();
        noteBlockRepository.save(noteBlock);

        BlockContext blockContext = blockContextService.createBlockContext(blockContextCreateRequestDTO, noteBlock);
        BlockContext blockContextTest = blockContextRepository.findById(blockContext.getId()).orElse(null);

        Assertions.assertAll(
                () -> assertNotEquals(null, blockContextTest, () -> "블럭 컨텍스트 생성이 제대로 되지 않았습니다"),
                () -> assertEquals("test", blockContextTest.getContext(), () -> "블럭 컨텍스트의 컨택스트가 제대로 생성 되지 않았습니다"),
                () -> assertEquals("H1", blockContextTest.getTag(), () -> "블럭 컨텍스트의 태그가 제대로 생성 되지 않았습니다"),
                () -> assertEquals(1L, blockContextTest.getIndex(), () -> "블럭 컨텍스트의 인덱스가 제대로 생성 되지 않았습니다")
        );
    }

    @Test
    @DisplayName("블럭 컨텍스트 업데이트 테스트")
    @Transactional
    void updateBlockContext() {
        BlockContextCreateRequestDTO blockContextCreateRequestDTO = new BlockContextCreateRequestDTO();
        blockContextCreateRequestDTO.setContext("test");
        blockContextCreateRequestDTO.setTag("H1");
        blockContextCreateRequestDTO.setIndex(1L);

        NoteBlock noteBlock = new NoteBlock();
        noteBlockRepository.save(noteBlock);

        BlockContext blockContext = blockContextService.createBlockContext(blockContextCreateRequestDTO, noteBlock);

        BlockContextUpdateRequestDTO blockContextUpdateRequestDTO = new BlockContextUpdateRequestDTO();
        blockContextUpdateRequestDTO.setContext("update");
        blockContextUpdateRequestDTO.setTag("H2");
        blockContextUpdateRequestDTO.setIndex(2L);

        blockContext = blockContextService.updateBlockContext(blockContextUpdateRequestDTO, noteBlock);
        BlockContext blockContextTest = blockContextRepository.findById(blockContext.getId()).orElse(null);

        Assertions.assertAll(
                () -> assertNotEquals(null, blockContextTest, () -> "블럭 컨텍스트 생성이 제대로 되지 않았습니다"),
                () -> assertEquals("update", blockContextTest.getContext(), () -> "블럭 컨텍스트의 컨택스트가 제대로 업데이트 되지 않았습니다"),
                () -> assertEquals("H2", blockContextTest.getTag(), () -> "블럭 컨텍스트의 태그가 제대로 업데이트 되지 않았습니다"),
                () -> assertEquals(2L, blockContextTest.getIndex(), () -> "블럭 컨텍스트의 인덱스가 제대로 업데이트 되지 않았습니다")
        );
    }
}