# SPRING BOOT JSON VALIDATOR

## Synopsis

The project is a Spring Boot Application that validates a Json request based on a Json schema. 

## Motivation

Sometimes you need to validate a json document.
For example:
<ul>
  <li>if a property is required</li>
  <li>if the data type of the property is the one you are expecting</li>
  <li>etc</li>
</ul>

This is what this project does. Is a Json validator.

The following link has all the things needed about Json Schema: 
[JSON Schema](https://json-schema.org/)

The following link has the Step-By-Step Json Schema:
[Json Schema Getting Started Step-By-Step](https://json-schema.org/learn/getting-started-step-by-step.html)

The following link has Implementation of the Json Schema for the different languages:
[JSON Schema Implementations](https://json-schema.org/implementations.html)

The following link has the implementation I used for this project:
[jsonschemafriend](https://github.com/jimblackler/jsonschemafriend)

## Pre Requirements

- You need a Json Schema. This is already in resources folder (test-schema.json).


USING POSTMAN:
--------------

HAPPY PATH 1
------------

All the fields are received with the correct value

POST
http://localhost:8067/json/validator


Body
----
```json
{
    "jsonSchemaName": "test-schema.json",
    "jsonData": {
        "id": 13,
        "name": "test",
        "description": "This Json file is a test",
        "field_not_required": "Not required",
        "cost": 8999.99,
        "active": true,
        "any_date": "2022-11-13"
    }
}
```

Response:
---------
```json
{
    "timestamp": "2022-04-26T02:49:45.756+00:00",
    "status": 200,
    "error": "OK",
    "message": "Json received OK",
    "path": "/json/validator"
}
```

Eclipse Console (HAPPY PATH 1):
-------------------------------
```log

2022-04-25 21:49:45.738  INFO 5132 --- [nio-8067-exec-2] r.a.c.j.v.c.JsonValidatorController      : Json Schema:  ##### test-schema.json #####  validated and works fine.


```


HAPPY PATH 2
------------
The following field "field_not_required" is missing but it is not required, so it is OK 

POST
http://localhost:8067/json/validator

Body
----
```json
{
    "jsonSchemaName": "test-schema.json",
    "jsonData": {
        "id": 13,
        "name": "test",
        "description": "This Json file is a test",
        "cost": 8999.99,
        "active": true,
        "any_date": "2022-11-13"
    }
}
```

Response:
---------
```json
{
    "timestamp": "2022-04-26T02:52:40.387+00:00",
    "status": 200,
    "error": "OK",
    "message": "Json received OK",
    "path": "/json/validator"
}
```

Eclipse Console (HAPPY PATH 2):
-------------------------------
```log

2022-04-25 21:52:40.371  INFO 20336 --- [nio-8067-exec-1] r.a.c.j.v.c.JsonValidatorController      : Json Schema:  ##### test-schema.json #####  validated and works fine.

```

WHEN A REQUIRED PROPERTY IS MISSING
-----------------------------------
The field "id" is required. And in this example is missing.

POST
http://localhost:8067/json/validator

Body
----
```json
{
    "jsonSchemaName": "test-schema.json",
    "jsonData": {
        "name": "test",
        "description": "This Json file is a test",
        "field_not_required": "Not required",
        "cost": 8999.99,
        "active": true,
        "any_date": "2022-11-13"
    }
}
```

Response:
---------
```json
{
    "timestamp": "2022-04-26T02:54:41.074+00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Missing data: Validation errors: {\"name\":\"test\",\"description\":\"This Json file is a test\",\"... at root failed against file:/C:/RAC/workspaceSpringBoot/spring-boot-json-validator/target/classes/test-schema.json with \"Missing property id\"",
    "path": "/json/validator"
}
```

Eclipse Console (WHEN A REQUIRED PROPERTY IS MISSING):
------------------------------------------------------
```log

2022-04-25 21:54:41.053 ERROR 9936 --- [nio-8067-exec-1] r.a.c.j.v.c.JsonValidatorController      :  ##### ERROR: Validation errors: {"name":"test","description":"This Json file is a test","... at root failed against file:/C:/RAC/workspaceSpringBoot/spring-boot-json-validator/target/classes/test-schema.json with "Missing property id"

```

WHEN A PROPERTY DATA TYPE IS INCORRECT
--------------------------------------
The field "cost" is of type number. In this example is a string.

POST
http://localhost:8067/json/validator

Body
----
```json
{
    "jsonSchemaName": "test-schema.json",
    "jsonData": {
        "id": 13,
        "name": "test",
        "description": "This Json file is a test",
        "field_not_required": "Not required",
        "cost": "hi",
        "active": true,
        "any_date": "2022-11-13"
    }
}
```

Response:
---------
```json
{
    "timestamp": "2022-04-26T02:56:00.046+00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Missing data: Validation errors: \"hi\" at #/cost failed against file:/C:/RAC/workspaceSpringBoot/spring-boot-json-validator/target/classes/test-schema.json#/properties/cost with \"Expected: [number] Found: [string]\"",
    "path": "/json/validator"
}
```

Eclipse Console (WHEN A PROPERTY DATA TYPE IS INCORRECT):
---------------------------------------------------------
```log

2022-04-25 21:56:00.027 ERROR 20040 --- [nio-8067-exec-3] r.a.c.j.v.c.JsonValidatorController      :  ##### ERROR: Validation errors: "hi" at #/cost failed against file:/C:/RAC/workspaceSpringBoot/spring-boot-json-validator/target/classes/test-schema.json#/properties/cost with "Expected: [number] Found: [string]"

```

YOU CAN DO THE SAME TESTS AS ABOVE WITH THE FOLLOWING
-----------------------------------------------------
POST
http://localhost:8067/json/validator

Body
----
```json
{
    "jsonSchema": {
        "$schema": "https://json-schema.org/draft/2020-12/schema",
        "title": "Test",
        "description": "Json Test",
        "type": "object",
        "properties": {
            "id": {
                "description": "Identifier",
                "type": "integer"
            },
            "name": {
                "description": "Name to test",
                "type": "string"
            },
            "description": {
                "description": "Description to test",
                "type": "string"
            },
            "field_not_required": {
                "description": "This field is optional",
                "type": "string"
            },
            "cost": {
                "description": "Cost to test",
                "type": "number"
            },
            "active": {
                "description": "Is active",
                "type": "boolean"
            },
            "any_date": {
                "description": "Any date",
                "type": "string",
                "format": "date"
            }
        },
        "required": [
            "id",
            "name",
            "cost",
            "any_date",
            "active"
        ]
    },
    "jsonData": {
        "id": 13,
        "name": "test",
        "description": "This Json file is a test",
        "field_not_required": "Not required",
        "cost": 8999.99,
        "active": true,
        "any_date": "2022-11-13"
    }
}
```

Response:
---------
```json
{
    "timestamp": "2022-04-26T02:59:15.115+00:00",
    "status": 200,
    "error": "OK",
    "message": "Json received OK",
    "path": "/json/validator"
}
```

Eclipse Console (WHEN A REQUIRED PROPERTY IS MISSING):
------------------------------------------------------
```log

2022-04-25 21:59:15.102  INFO 18108 --- [nio-8067-exec-2] r.a.c.j.v.c.JsonValidatorController      : Json Schema:  ##### {"$schema":"https://json-schema.org/draft/2020-12/schema","title":"Test","description":"Json Test","type":"object","properties":{"id":{"description":"Identifier","type":"integer"},"name":{"description":"Name to test","type":"string"},"description":{"description":"Description to test","type":"string"},"field_not_required":{"description":"This field is optional","type":"string"},"cost":{"description":"Cost to test","type":"number"},"active":{"description":"Is active","type":"boolean"},"any_date":{"description":"Any date","type":"string","format":"date"}},"required":["id","name","cost","any_date","active"]} #####  validated and works fine.

```

## License

All work is under Apache 2.0 license