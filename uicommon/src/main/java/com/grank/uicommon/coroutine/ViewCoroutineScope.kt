package com.grank.uicommon.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

abstract class ViewCoroutineScope internal constructor() : CoroutineScope

class ViewCoroutineScopeImp(override val coroutineContext: CoroutineContext) :
    ViewCoroutineScope()
