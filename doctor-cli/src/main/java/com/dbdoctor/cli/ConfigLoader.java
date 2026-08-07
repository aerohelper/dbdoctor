package com.dbdoctor.cli;

import com.dbdoctor.core.config.DoctorConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.nio.file.Path;

/** Loads a {@link DoctorConfig} from a YAML file, or returns all-default config if none is given. */
final class ConfigLoader {

    private static final ObjectMapper YAML_MAPPER = new ObjectMapper(new YAMLFactory());

    private ConfigLoader() {
    }

    static DoctorConfig load(Path configPath) throws IOException {
        if (configPath == null) {
            return new DoctorConfig();
        }
        return YAML_MAPPER.readValue(configPath.toFile(), DoctorConfig.class);
    }
}
