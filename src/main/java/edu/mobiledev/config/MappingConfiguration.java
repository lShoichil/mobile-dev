package edu.mobiledev.config;

import java.util.*;

import edu.mobiledev.mapper.*;
import lombok.*;
import org.modelmapper.*;
import org.springframework.context.annotation.*;

import static org.modelmapper.config.Configuration.AccessLevel.*;
import static org.modelmapper.convention.MatchingStrategies.*;

@Configuration
@AllArgsConstructor
public class MappingConfiguration {

    private List<Mapper> mappers;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
            .setMatchingStrategy(STRICT)
            .setFieldMatchingEnabled(true)
            .setSkipNullEnabled(true)
            .setFieldAccessLevel(PRIVATE);

        mappers.forEach(m -> m.init(modelMapper));

        return modelMapper;
    }

}

