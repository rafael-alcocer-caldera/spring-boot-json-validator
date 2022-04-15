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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final JsonValidatorConfiguration config;

    @ResponseStatus(code = HttpStatus.OK, reason = "Json received OK")
    @GetMapping("/validator")
    @ResponseBody
    public void sendMessage(@RequestBody Model model) {
        try {
            LOGGER.info("##### Json Schema Name: " + config.getFileName());

            Resource resource = new ClassPathResource(config.getFileName());
            // InputStream input = resource.getInputStream(); // It is necessary although it
            // looks like not

            // If the following validation is not correct, then will go to catch
            config.validator().validate(config.schemaStore().loadSchema(resource.getFile()), model.getJson());

            LOGGER.info("##### If you get here... it means that the validation was OK...");
            LOGGER.info("##### This is the Json request: " + model.getJson());
        } catch (ValidationException | GenerationException | IOException e) {
            LOGGER.error("##### ERROR: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Missing data: " + e.getMessage(), e);
        }
    }
}
