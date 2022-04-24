package com.miko.story.domain.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    val id: String,
    val photoUrl: String,
    val name: String,
    val description: String,
    val latLng: LatLng,
) : Parcelable
