package ru.sushchenko.taskmanagement.utils.mapper;

import org.modelmapper.ModelMapper;

public class CustomModelMapper extends ModelMapper {
    // Customized map method in order to be able to map to nullable objects
    @Override
    public <D> D map(Object source, Class<D> destinationType) {
        Object tmpSource = source;
        if(source == null){
            tmpSource = new Object();
        }

        return super.map(tmpSource, destinationType);
    }
}
