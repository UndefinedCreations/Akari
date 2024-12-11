package com.undefined.akari.exception

class NegativeNumberException(
    val number: Int
) : RuntimeException("The number is you are trying to use is a negative number what is not support in this arena. ($number)")