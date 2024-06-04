package com.wridge.util

@Suppress("UNCHECKED_CAST")
fun Map<String?, Any?>.safeUnwrapOptionalMap(): Map<String, Any> = filter { it.key != null && it.value != null } as Map<String, Any>
