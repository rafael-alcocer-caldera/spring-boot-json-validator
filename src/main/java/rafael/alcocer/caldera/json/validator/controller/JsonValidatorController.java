/**
 * Copyright [2022] [RAFAEL ALCOCER CALDERA]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rafael.alcocer.caldera.json.validator.controller;

import java.io.IOException;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import net.jimblackler.jsonschemafriend.GenerationException;
import net.jimblackler.jsonschemafriend.ValidationException;
import rafael.alcocer.caldera.json.validator.configuration.JsonValidatorConfiguration;
import rafael.alcocer.caldera.json.validator.model.Model;

@RequiredArgsConstructor
@RestController
@RequestMapping("/json")
public class JsonValidatorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonValidatorController.class);

    private static final String ERROR = "ERROR: ";
    private static final String HASH_TAG = " ##### ";
    private static final String JSON_RECEIVED_OK = "Json received OK";
    private static final String JSON_SCHEMA = "Json Schema: ";
    private static final String MISSING_DATA = "Missing data: ";
    private static final String NOT_NULL_MESSAGE = "jsonSchemaName or jsonSchema in the request Body must not be null, empty, or whitespace only";
    private static final String VALIDATED = " validated and works fine.";

    private final JsonValidatorConfiguration config;

    @ResponseStatus(code = HttpStatus.OK, reason = JSON_RECEIVED_OK)
    @PostMapping("/validator")
    @ResponseBody
    public void validateJson(@RequestBody Model model) {
        try {
            if (StringUtils.isNotBlank(model.getJsonSchemaName())) {
                Resource resource = new ClassPathResource(model.getJsonSchemaName());

                // If the following validation is not correct, then will go to catch
                config.validator().validate(config.schemaStore().loadSchema(resource.getFile()), model.getJsonData());

                LOGGER.info(JSON_SCHEMA + HASH_TAG + model.getJsonSchemaName() + HASH_TAG + VALIDATED);

                return;
            }

            if (ObjectUtils.isNotEmpty(model.getJsonSchema())) {
                // If the following validation is not correct, then will go to catch
                config.validator().validate(config.schemaStore().loadSchema(model.getJsonSchema()), model.getJsonData());

                LOGGER.info(JSON_SCHEMA + HASH_TAG + model.getJsonSchema() + HASH_TAG + VALIDATED);

                return;
            }

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, NOT_NULL_MESSAGE);
        } catch (ValidationException | GenerationException | IOException e) {
            LOGGER.error(HASH_TAG + ERROR + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MISSING_DATA + e.getMessage(), e);
        }
    }
}
