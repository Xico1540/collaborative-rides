package com.example.cmu_trabalho.ui.screens

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.viewport.data.FollowPuckViewportStateBearing
import com.mapbox.maps.plugin.viewport.data.FollowPuckViewportStateOptions
import com.mapbox.maps.plugin.viewport.viewport
@SuppressLint("RememberReturnType")
@Composable
fun MapSelectionScreen(
    onLocationSelected: (latitude: Double, longitude: Double, isPartida: Boolean) -> Unit,
    isPartida: Boolean
) {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            location.updateSettings {
                enabled = true
                pulsingEnabled = true
            }
            // Habilitar localização do usuário
            location.locationPuck = createDefault2DPuck(withBearing = true)
            location.puckBearing = PuckBearing.COURSE
            location.puckBearingEnabled = true
        }
    }

    AndroidView(
        factory = { mapView },
    ) { mapView ->
        // Adiciona o listener de clique no mapa
        mapView.gestures.addOnMapClickListener { point ->
            onLocationSelected(point.latitude(), point.longitude(), isPartida)
            true
        }

        // Seguir a localização do usuário
        val viewportPlugin = mapView.viewport
        val followPuckViewportState = viewportPlugin.makeFollowPuckViewportState(
            FollowPuckViewportStateOptions.Builder()
                .bearing(FollowPuckViewportStateBearing.Constant(0.0)) // Definir a orientação do usuário
                .build()
        )
        viewportPlugin.transitionTo(followPuckViewportState)
    }
}

