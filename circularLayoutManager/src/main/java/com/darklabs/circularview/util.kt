package com.darklabs.circularview

import androidx.annotation.FloatRange
import androidx.annotation.IntDef
import androidx.recyclerview.widget.RecyclerView


/**
 * Valid gravity settings for a [CircularLayoutManager].  This defines the direction of the center point
 * around which items will rotate.
 */
@IntDef(value = [Gravity.START, Gravity.END])
annotation class Gravity {
    companion object {
        const val START = android.view.Gravity.START
        const val END = android.view.Gravity.END
    }
}

/**
 * Orientation as defined in [RecyclerView]
 */
@IntDef(value = [Orientation.VERTICAL, Orientation.HORIZONTAL])
annotation class Orientation {
    companion object {
        const val VERTICAL = RecyclerView.VERTICAL
        const val HORIZONTAL = RecyclerView.HORIZONTAL
    }
}

fun createGravityFromAttr(attr: Int): Int {
    return when (attr) {
        0 -> Gravity.START
        1 -> Gravity.END
        else -> throw IllegalArgumentException("Attribute not supported")
    }
}

fun createOrientationFromAttr(attr: Int): Int {
    return when (attr) {
        0 -> Orientation.HORIZONTAL
        1 -> Orientation.VERTICAL
        else -> throw IllegalArgumentException("Attribute not supported")
    }
}

const val MIN_RADIUS = 0
const val MIN_PEEK = 0

const val DEFAULT_RADIUS = 2000
const val DEFAULT_PEEK = 500