# TODO
1.  [x] cache with CacheDeque
2.  [x] cache with HashMap
3.  [x] exception interceptors like in original
4.  [x] kafka producer / consumer
5.  [x] simple rest controller
6.  [x] H2 database
7.  [x] jwt/jwks example
8.  [ ] inter-apps example (http requests to another app)
9.  [ ] DB connect
10. [x] decoration-or-default function (on steroids!)
11. [ ] session host function

# Diffs from original project:
1. MDC impl - traceId creation/propagation among rest/kafka services
2. Inteceptors instead of aspects
3. Swagger and simple documentation


# *Mutable reponses documentation*

With all classes in Template package - its example for mutability of responses.

## Chain of calls
| step | component | action |
|---|---|---|
| 1. | RestController | Entry point |
| 2. | HTTP method | Takes JsonNode as input, mandatory id field. Others - can be used as values in template rendering |
| 3. | ResponseRouter|Takes id as input and prepares combined object <templateName/templateRenderer, templateJsonNode> |
| 4. | BindingRegistry | Id in cache (binded) ? return corresponding templateName : return default templateName |
| 5. | TemplateProvider | At startup: scan resources for .json's. At request (by templateName): returns JsonNode of template |
| 6. | RendererDispatcher | At startup: scan beans of app for renderers (@component + interface templateRenderer). At request: applies rendering to template |
| 7. | Response HTTP | Return JsonNode as result |

## Additions:
| step | component | action |
|---|---|---|
| 1. | TempalteCoverageCheck | at startup checks that for each .json there is render class (String name of class must be equal to .json name, made via @component and interface) |
| 2. | BindingRegistry | auto swipe for outdated bindings (can be modified via app props) |
| 3. | BindingRegistry | take/remove logic for binding (if for requested ID there was binding and it outdated: binding deleted, returns default template) |
