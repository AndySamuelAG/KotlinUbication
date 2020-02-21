package com.example.ubicacion

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener
import kotlin.arrayOf as arrayOf1

class MainActivity : AppCompatActivity() {
    private val permisoFineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val permisoCoarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION

    private val CODIGO_SOLICITUD_PERMISO = 100

    var fusedLocationClient: FusedLocationProviderClient? = null

    var locationRequest : LocationRequest? = null

    var callback:LocationCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = FusedLocationProviderClient(this)
        inicializarLocationRequest()

    }

    private fun inicializarLocationRequest(){
        locationRequest = LocationRequest()
        locationRequest?.interval = 10000
        locationRequest?.fastestInterval = 5000
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun validarPermisosUbicacion(): Boolean{
        val hayUbicacionPrecisa = ActivityCompat.checkSelfPermission(this, permisoFineLocation) == PackageManager.PERMISSION_GRANTED
        val hayUbicacionOrdinaria = ActivityCompat.checkSelfPermission(this, permisoCoarseLocation) == PackageManager.PERMISSION_GRANTED

        return hayUbicacionOrdinaria && hayUbicacionOrdinaria
    }

//    @SuppressLint("MissingPermission")
    private fun obtenerUbicacion(){
//        fusedLocationClient?.lastLocation?.addOnSuccessListener (this, object: OnSuccessListener<Location>{
//
//            override fun onSuccess(location: Location?) {
//                if(location != null){
//                    Toast.makeText(applicationContext, location?.latitude.toString() + " - " + location?.longitude.toString(), Toast.LENGTH_LONG).show()
//                }
//            }
//        })

    callback = object: LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)
            for(ubicacion in locationResult?.locations!!){
                Toast.makeText(applicationContext, ubicacion.latitude.toString() + " --  "+ ubicacion.longitude.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fusedLocationClient?.requestLocationUpdates(locationRequest, callback, null)
    }

    private fun pedirPermisos(){
        val deboProveerContexto = ActivityCompat.shouldShowRequestPermissionRationale(this, permisoFineLocation)

        if(deboProveerContexto){
            //Mandar mensaje con explicación
        }

        solicitudPermisos()
    }

    private fun solicitudPermisos(){
        requestPermissions(
            kotlin.arrayOf(permisoCoarseLocation, permisoFineLocation), CODIGO_SOLICITUD_PERMISO
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            CODIGO_SOLICITUD_PERMISO->{
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Obtener ubicación
                }else{
                    Toast.makeText(this, "No hay permisos para acceder a la ubicación", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStart(){
        super.onStart()

        if(validarPermisosUbicacion()){
            obtenerUbicacion()
        }else{
            pedirPermisos()
        }
    }

    private fun detenerActualizacionUbicacion(){
        fusedLocationClient?.removeLocationUpdates(callback)
    }

    override fun onPause() {
        super.onPause()

        detenerActualizacionUbicacion()
    }
}
