package com.example.userservice.config;

import com.example.userservice.dto.UtenteDTO;
import com.example.userservice.entities.Utente;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        //modelMapper.addMappings(utenteDTOpropertyMap);

        return modelMapper;
    }
    /*
    PropertyMap<Utente, UtenteDTO> utenteDTOpropertyMap = new PropertyMap<Utente, UtenteDTO>() {
        @Override
        protected void configure() {

        }
    }

     */


}
