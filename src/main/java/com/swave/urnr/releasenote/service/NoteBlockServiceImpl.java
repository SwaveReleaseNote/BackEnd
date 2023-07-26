package com.swave.urnr.releasenote.service;

import com.swave.urnr.releasenote.domain.NoteBlock;
import com.swave.urnr.releasenote.domain.ReleaseNote;
import com.swave.urnr.releasenote.repository.NoteBlockRepository;
import com.swave.urnr.releasenote.requestdto.NoteBlockCreateRequestDTO;
import com.swave.urnr.releasenote.requestdto.NoteBlockUpdateRequestDTO;
import com.swave.urnr.releasenote.responsedto.ReleaseNoteLabelContentResponseDTO;
import com.swave.urnr.releasenote.responsedto.ReleaseNoteLabelCountResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableTransactionManagement
public class NoteBlockServiceImpl implements NoteBlockService {
    private final NoteBlockRepository noteBlockRepository;

    @Override
    @Transactional
    public NoteBlock createNoteBlock(NoteBlockCreateRequestDTO noteBlockCreateRequestDTO, ReleaseNote releaseNote){
        NoteBlock noteBlock = NoteBlock.builder()
                .label(noteBlockCreateRequestDTO.getLabel())
                .releaseNote(releaseNote)
                .build();

        noteBlockRepository.saveAndFlush(noteBlock);

        return noteBlock;
    }

    //todo 현재 업데이트 방법은 전체를 지우고 다시 생성하는 방식임, 로그를 남기는 점은 좋지만 그 외에는 별로임 업데이트 방법을 고민해 볼것
    @Override
    @Transactional
    public NoteBlock updateNoteBlock(NoteBlockUpdateRequestDTO noteBlockUpdateRequestDTO, ReleaseNote releaseNote){
        NoteBlock noteBlock = NoteBlock.builder()
                .label(noteBlockUpdateRequestDTO.getLabel())
                .releaseNote(releaseNote)
                .build();

        noteBlockRepository.saveAndFlush(noteBlock);

        return noteBlock;
    }

    @Override
    @Transactional
    public void deleteNoteBlock(Long noteBlockId){
        noteBlockRepository.deleteById(noteBlockId);
    }

    @Override
    public List<ReleaseNoteLabelCountResponseDTO> countLabel(Long projectId){
        return noteBlockRepository.countByLabel(projectId);
    }

    @Override
    public List<ReleaseNoteLabelContentResponseDTO> filterByLabel(Long projectId){
        List<ReleaseNoteLabelContentResponseDTO> releaseNoteLabelContentResponseDTOList = noteBlockRepository.filterByLabel(projectId);

        for (int i = 0 ; i < releaseNoteLabelContentResponseDTOList.toArray().length ; i++){
            String context = String.join("",releaseNoteLabelContentResponseDTOList.get(i).getContext());
            releaseNoteLabelContentResponseDTOList.get(i).setContext(new ArrayList<>(List.of(context)));
        }

        return releaseNoteLabelContentResponseDTOList;
    }
}
