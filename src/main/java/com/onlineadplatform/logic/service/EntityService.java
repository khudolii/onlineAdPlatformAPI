package com.onlineadplatform.logic.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class EntityService<ENTITY, DTO>{

    /*
    * The DTO object will be migrated to the ENTITY object
    * */
    public ENTITY getEntity(DTO dto) {
        if (dto == null) {
            return null;
        }
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Class<ENTITY> entityClass = (Class<ENTITY>)((ParameterizedType)getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];
        return modelMapper.map(dto, entityClass);
    }

    /*
    * The ENTITY object will be migrated to the DTO object
    * */
    public DTO getDTO(ENTITY entity) {
        if (entity == null) {
            return null;
        }
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Class<DTO> dtoClass = (Class<DTO>)((ParameterizedType)getClass().getGenericSuperclass())
                .getActualTypeArguments()[1];
        return modelMapper.map(entity, dtoClass);
    }

    public List<ENTITY> getEntitiesList(List<DTO> dtosList) {
        if (dtosList == null || dtosList.isEmpty()) {
            return new ArrayList<>();
        }
        return dtosList
                .stream()
                .map(this::getEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<DTO> getDTOsList(List<ENTITY> entitiesList) {
        if (entitiesList == null || entitiesList.isEmpty()) {
            return new ArrayList<>();
        }
        return entitiesList
                .stream()
                .map(this::getDTO)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
