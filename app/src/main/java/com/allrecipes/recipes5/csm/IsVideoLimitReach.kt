package com.allrecipes.recipes5.csm

interface IsVideoLimitReach {
    fun onVideoLimitReach(videoLimitReach: Boolean, isError: Boolean)
}