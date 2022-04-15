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

GET
http://localhost:8067/json/validator


Body
----
```json
{
	"json": {
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
    "timestamp": "2022-04-15T04:54:12.398+00:00",
    "status": 200,
    "error": "OK",
    "message": "Json received OK",
    "path": "/json/validator"
}
```

Eclipse Console (HAPPY PATH 1):
-------------------------------
```log

2022-04-14 23:54:10.555  INFO 16372 --- [nio-8067-exec-2] r.a.c.j.v.c.JsonValidatorController      : ##### Json Schema Name: test-schema.json

2022-04-14 23:54:12.381  INFO 16372 --- [nio-8067-exec-2] r.a.c.j.v.c.JsonValidatorController      : ##### If you get here... it means that the validation was OK...

2022-04-14 23:54:12.381  INFO 16372 --- [nio-8067-exec-2] r.a.c.j.v.c.JsonValidatorController      : ##### This is the Json request: {"id":13,"name":"test","description":"This Json file is a test","field_not_required":"Not required","cost":8999.99,"active":true,"any_date":"2022-11-13"}

```


HAPPY PATH 2
------------
The following field "field_not_required" is missing but it is not required, so it is OK 

GET
http://localhost:8067/json/validator

Body
----
```json
{
	"json": {
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
    "timestamp": "2022-04-15T04:56:46.241+00:00",
    "status": 200,
    "error": "OK",
    "message": "Json received OK",
    "path": "/json/validator"
}
```

Eclipse Console (HAPPY PATH 2):
-------------------------------
```log

2022-04-14 23:56:44.692  INFO 16700 --- [nio-8067-exec-1] r.a.c.j.v.c.JsonValidatorController      : ##### Json Schema Name: test-schema.json

2022-04-14 23:56:46.218  INFO 16700 --- [nio-8067-exec-1] r.a.c.j.v.c.JsonValidatorController      : ##### If you get here... it means that the validation was OK...

2022-04-14 23:56:46.219  INFO 16700 --- [nio-8067-exec-1] r.a.c.j.v.c.JsonValidatorController      : ##### This is the Json request: {"id":13,"name":"test","description":"This Json file is a test","cost":8999.99,"active":true,"any_date":"2022-11-13"}

```

WHEN A REQUIRED PROPERTY IS MISSING
-----------------------------------
The field "id" is required. And in this example is missing.

GET
http://localhost:8067/json/validator

Body
----
```json
{
	"json": {
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
    "timestamp": "2022-04-15T21:50:20.945+00:00",
    "status": 417,
    "error": "Expectation Failed",
    "message": "Missing data: Validation errors: {\"name\":\"test\",\"description\":\"This Json file is a test\",\"... at root failed against file:/C:/RAC/workspaceSpringBoot/spring-boot-json-validator/target/classes/test-schema.json with \"Missing property id\"",
    "path": "/json/validator"
}
```

Eclipse Console (WHEN A REQUIRED PROPERTY IS MISSING):
------------------------------------------------------
```log

2022-04-15 16:50:18.878  INFO 18916 --- [nio-8067-exec-1] r.a.c.j.v.c.JsonValidatorController      : ##### Json Schema Name: test-schema.json

2022-04-15 16:50:20.928 ERROR 18916 --- [nio-8067-exec-1] r.a.c.j.v.c.JsonValidatorController      : ##### ERROR: Validation errors: {"name":"test","description":"This Json file is a test","... at root failed against file:/C:/RAC/workspaceSpringBoot/spring-boot-json-validator/target/classes/test-schema.json with "Missing property id"

```

WHEN A PROPERTY DATA TYPE IS INCORRECT
--------------------------------------
The field "cost" is of type number. In this example is a string.

GET
http://localhost:8067/json/validator

Body
----
```json
{
	"json": {
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
    "timestamp": "2022-04-15T21:54:43.221+00:00",
    "status": 417,
    "error": "Expectation Failed",
    "message": "Missing data: Validation errors: \"hi\" at #/cost failed against file:/C:/RAC/workspaceSpringBoot/spring-boot-json-validator/target/classes/test-schema.json#/properties/cost with \"Expected: [number] Found: [string]\"",
    "path": "/json/validator"
}
```

Eclipse Console (WHEN A PROPERTY DATA TYPE IS INCORRECT):
---------------------------------------------------------
```log

2022-04-15 16:54:41.620  INFO 19092 --- [nio-8067-exec-1] r.a.c.j.v.c.JsonValidatorController      : ##### Json Schema Name: test-schema.json

2022-04-15 16:54:43.201 ERROR 19092 --- [nio-8067-exec-1] r.a.c.j.v.c.JsonValidatorController      : ##### ERROR: Validation errors: "hi" at #/cost failed against file:/C:/RAC/workspaceSpringBoot/spring-boot-json-validator/target/classes/test-schema.json#/properties/cost with "Expected: [number] Found: [string]"


```

## License

All work is under Apache 2.0 license