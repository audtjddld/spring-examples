package com.example.training.exception

import com.linecorp.armeria.common.RequestContext
import com.linecorp.armeria.common.grpc.GrpcStatusFunction
import io.grpc.Metadata
import io.grpc.Status

class GrpcExceptionHandler : GrpcStatusFunction {
    override fun apply(ctx: RequestContext, throwable: Throwable, metadata: Metadata): Status? {
        if (throwable is IllegalArgumentException) {
            return Status.INVALID_ARGUMENT.withCause(throwable)
        }
        if (throwable is BlogNotFoundException) {
            return Status.NOT_FOUND.withCause(throwable).withDescription(throwable.message)
        }
        return null
    }
}
