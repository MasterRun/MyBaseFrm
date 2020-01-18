package com.jsongo.core.network

import com.jsongo.core.bean.ErrorDataWrapper

class NetFailedException(
    val errorDataWrapper: ErrorDataWrapper = ErrorDataWrapper.DEFAULT
) : Exception(errorDataWrapper.message)