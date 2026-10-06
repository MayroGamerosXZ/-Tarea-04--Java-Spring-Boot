package com.thundermax.ferreteria.exception;

/**
 * Se lanza cuando una operación viola una regla del negocio → HTTP 409 (Conflict).
 * Ej.: vender más de lo que hay en stock, anular una venta ya anulada,
 * registrar un NIT repetido.
 */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
