package com.example.training.exception


internal class BlogNotFoundException(s: String?) : IllegalStateException(s) {
    companion object {
        private const val serialVersionUID = -2914549282978136686L
    }
}
