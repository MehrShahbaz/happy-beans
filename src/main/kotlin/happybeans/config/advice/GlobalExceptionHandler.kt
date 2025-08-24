package happybeans.config.advice

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import happybeans.dto.error.ErrorResponse
import happybeans.utils.exception.DuplicateEntityException
import happybeans.utils.exception.EntityNotFoundException
import happybeans.utils.exception.UnauthorisedUserException
import happybeans.utils.exception.UserAlreadyExistsException
import happybeans.utils.exception.UserCredentialException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEmptyResult(
        err: EntityNotFoundException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        return errorResponse(HttpStatus.NOT_FOUND, "${err.message}", request)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        val rootCause = ex.cause
        val message =
            when (rootCause) {
                is MismatchedInputException -> {
                    val fieldName = rootCause.path?.firstOrNull()?.fieldName ?: "unknown"
                    "Missing or invalid value for field: '$fieldName'"
                }
                else -> "Invalid request payload"
            }
        return errorResponse(HttpStatus.BAD_REQUEST, message, request)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        val errors = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "Invalid value") }
        return errorResponse(HttpStatus.BAD_REQUEST, errors, request)
    }

    @ExceptionHandler(UnauthorisedUserException::class)
    fun handleUnauthorisedUserException(
        ex: UnauthorisedUserException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        return errorResponse(HttpStatus.UNAUTHORIZED, ex.message ?: "UNAUTHORIZED", request)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        ex: IllegalArgumentException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        return errorResponse(HttpStatus.BAD_REQUEST, ex.message ?: "BAD_REQUEST", request)
    }

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExistsException(
        ex: UserAlreadyExistsException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        return errorResponse(HttpStatus.CONFLICT, ex.message ?: "Already exists", request)
    }

    @ExceptionHandler(UserCredentialException::class)
    fun handleUserCredentialException(
        ex: UserCredentialException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        return errorResponse(HttpStatus.UNAUTHORIZED, ex.message ?: "Already exists", request)
    }

    @ExceptionHandler(DuplicateEntityException::class)
    fun handleDuplicateEntityException(
        ex: DuplicateEntityException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        return errorResponse(HttpStatus.CONFLICT, ex.message ?: "Already exists", request)
    }

    private fun errorResponse(
        status: HttpStatus,
        message: Any,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        val error = status.reasonPhrase
        val response =
            ErrorResponse(
                status = status.value(),
                error = error,
                message = message,
                path = request.requestURI,
            )
        return ResponseEntity.status(status).body(response)
    }
}
