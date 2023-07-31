package com.swave.urnr.util.mapper;

import com.swave.urnr.releasenote.domain.ReleaseNote;
import com.swave.urnr.releasenote.requestdto.ReleaseNoteUpdateRequestDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ReleaseNoteMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateReleaseNoteByRequest(ReleaseNoteUpdateRequestDTO releaseNoteUpdateRequestDTO, @MappingTarget ReleaseNote releaseNote);
}
