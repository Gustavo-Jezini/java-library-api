package io.github.com.libraryapi.controller.common;

import io.github.com.libraryapi.controller.dto.ErroCampoDTO;
import io.github.com.libraryapi.controller.dto.ErroRespostaDTO;
import io.github.com.libraryapi.exceptions.CampoInvalidoException;
import io.github.com.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.com.libraryapi.exceptions.RegistroDuplicadoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroRespostaDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getFieldErrors();
        List<ErroCampoDTO> listaErros = fieldErrors
                .stream()
                .map(fe -> new ErroCampoDTO(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ErroRespostaDTO(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de validação",
                listaErros
        );
    }

    @ExceptionHandler(RegistroDuplicadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroRespostaDTO handleRegistroDuplicadoException(RegistroDuplicadoException e) {
        return ErroRespostaDTO.conflito(e.getMessage());
    }

    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroRespostaDTO handleOperacaoNaoPermitidaException(OperacaoNaoPermitidaException e) {
        return ErroRespostaDTO.respostaPadrao(e.getMessage());
    }

    @ExceptionHandler(CampoInvalidoException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroRespostaDTO handleCampoInvalidoException(CampoInvalidoException e) {
        List<ErroCampoDTO> listaErros = List.of(new ErroCampoDTO(
                e.getCampo(),
                e.getMessage()
        ));

        return new ErroRespostaDTO(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de validação",
                listaErros
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErroRespostaDTO handleAccessDeniedException(AccessDeniedException e) {
        return new ErroRespostaDTO(
                HttpStatus.FORBIDDEN.value(),
                "Acesso Negado.",
                List.of()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroRespostaDTO handleErrosNaoTratados(RuntimeException e) {
        return new ErroRespostaDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocorreu um erro inesperado. Entre em contato com a administração.",
                List.of()
        );
    }
}
