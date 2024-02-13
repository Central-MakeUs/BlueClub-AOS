package org.blueclub.presentation.type

enum class TosViewType (
    val essential: Boolean,
){
    AGE(true), SERVICE(true), PRIVACY(true), MARKETING(false), EVENT(false),
}