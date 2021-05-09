package com.grank.netcore.core

import androidx.annotation.IntDef
import com.grank.netcore.core.Resource.Status.Companion.FAIL
import com.grank.netcore.core.Resource.Status.Companion.LOADING
import com.grank.netcore.core.Resource.Status.Companion.SUCCESS


/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
data class Resource<out T>(
        val status: @Status Int,
        val data: T?,
        val errorCode: Int = 0,
        val message: String? = null
) {

    @Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(LOADING, FAIL, SUCCESS)
    annotation class Status {
        companion object {
            const val LOADING = 0
            const val FAIL = 1
            const val SUCCESS = 2
        }
    }

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(SUCCESS, data)
        }

        fun <T> fail(errorCode: Int, msg: String?, data: T? = null): Resource<T> {
            return Resource(FAIL, data, errorCode, msg)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(LOADING, data)
        }
    }
}

