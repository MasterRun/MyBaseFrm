package com.jsongo.mybasefrm.data.repository

import com.jsongo.core.bean.ErrorDataWrapper

class NetFailedException(
    val errorDataWrapper: ErrorDataWrapper = ErrorDataWrapper.DEFAULT
) : Exception(errorDataWrapper.message)