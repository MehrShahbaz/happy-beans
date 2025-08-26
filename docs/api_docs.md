# API

## Request and Response

### Request

* The **entity ID** is sent in the URL.
* For `@PutMapping`:

    * Include the request body only if needed.
    * The body **does not contain the ID**.

### Response

* **ResponseEntity with status and message** (for upload, update, get):

  ```kotlin
  // Example 1: return type
  return ResponseEntity.created(URI.create("/api/cart/$id")).body(MessageResponse("Product added to cart"))
  return ResponseEntity.ok(MessageResponse("Option updated"))
  ```

  ```kotlin
  // Example 2
  @PutMapping("/api/{productId}/options/{optionId}")
  fun updateOption(
      @PathVariable("productId") productId: Long,
      @PathVariable("optionId") optionId: Long,
      @RequestBody @Valid optionDTO: OptionDTO,
  ): ResponseEntity<MessageResponse> {
      adminProductService.updateOption(productId, optionId, optionDTO)
      return ResponseEntity.ok(MessageResponse("Option updated"))
  }
  ```

* **ResponseEntity with `noContent`**: for delete operations.
