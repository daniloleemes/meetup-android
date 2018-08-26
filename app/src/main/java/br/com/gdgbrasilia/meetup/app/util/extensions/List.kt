package br.com.gdgbrasilia.meetup.app.util.extensions

fun <T> List<T>.secondOrNull(): T? {
    return if (isEmpty()) null else this[1]
}

fun <T> List<T>.thirdOrNull(): T? {
    return if (isEmpty()) null else this[2]
}


fun <T> List<T>.getOrNull(index: Int): T? {
    return if (isEmpty()) null else this[index]
}