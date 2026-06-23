package com.zx_tole.pocketpsychologist.data.model

enum class MoodType(val value: Int) {
    NEUTRAL(0),
    CALM(1),
    EXCITED(2),
    ANXIOUS(3),
    SAD(4);

    companion object {
        fun fromValue(value: Int): MoodType {
            return entries.find { it.value == value } ?: NEUTRAL
        }
    }
}
