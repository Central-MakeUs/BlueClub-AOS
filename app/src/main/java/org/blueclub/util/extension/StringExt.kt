package org.blueclub.util.extension

import java.text.BreakIterator

fun String?.getStringLength(): Int {
    if (this == null) return 0
    val it: BreakIterator = BreakIterator.getCharacterInstance()
    it.setText(this)
    var count = 0
    while (it.next() != BreakIterator.DONE) {
        count++
    }
    return count
}