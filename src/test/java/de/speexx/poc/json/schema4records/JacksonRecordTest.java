package de.speexx.poc.json.schema4records;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.victools.jsonschema.generator.Option;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.javax.validation.JavaxValidationModule;
import com.github.victools.jsonschema.module.javax.validation.JavaxValidationOption;
import com.google.common.truth.Truth;
import java.io.StringWriter;
import org.junit.jupiter.api.Test;

/**
 *
 * @author sascha.kohlmann
 */
public class JacksonRecordTest {

    @Test
    public void serialize_deserialize() throws Exception {
        
        final var mapper = new ObjectMapper();
        
        final var testRecord = new JacksonRecord("some value");
        final var json = mapper.writeValueAsString(testRecord);
        final var deserialized = mapper.readValue(json, JacksonRecord.class);
        
        Truth.assertThat(deserialized.testValue()).isEqualTo("some value");

        System.out.println("JSON: " + json);
    }
    
    @Test
    public void generate_schema() {
        
        final var objectMapper = new ObjectMapper();
        objectMapper.writerWithDefaultPrettyPrinter();
        final var configBuilder = new SchemaGeneratorConfigBuilder(objectMapper, SchemaVersion.DRAFT_2019_09, OptionPreset.PLAIN_JSON);
        final var config = configBuilder.with(Option.SCHEMA_VERSION_INDICATOR,
                                              Option.DEFINITION_FOR_MAIN_SCHEMA,
                                              Option.PLAIN_DEFINITION_KEYS)
                                        .without(Option.NONSTATIC_NONVOID_NONGETTER_METHODS,
                                                 Option.PLAIN_DEFINITION_KEYS,
                                                 Option.TRANSIENT_FIELDS,
                                                 Option.VOID_METHODS)
                                        .with(new JacksonModule())
                                        .with(new JavaxValidationModule(JavaxValidationOption.INCLUDE_PATTERN_EXPRESSIONS, JavaxValidationOption.NOT_NULLABLE_FIELD_IS_REQUIRED))
                                        .build();
        final var generator = new SchemaGenerator(config);
        final var jsonSchema = generator.generateSchema(JacksonRecord.class); 

        final var writer = new StringWriter();
        writer.write(jsonSchema.toPrettyString());
        writer.flush();
        System.out.println("SCHEMA: " + writer.toString());
    }
}
