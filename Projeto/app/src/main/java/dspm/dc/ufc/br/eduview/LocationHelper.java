package dspm.dc.ufc.br.eduview;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.Semaphore;

public class LocationHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener {

    private GoogleApiClient googleApiClient;
    private Context callerContext;
    private Criteria criteria;
    private String bestProvider;
    private LocationManager locationManager;

    public LocationHelper(Context context) {
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(context);
        callerContext = context;
        builder.addApi(LocationServices.API);
        builder.addConnectionCallbacks(this);
        builder.addOnConnectionFailedListener(this);
        googleApiClient = builder.build();

        googleApiClient.connect();
    }

    public void disconnect() {
        googleApiClient.disconnect();
    }

    public void getLocation() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
                if (ActivityCompat.checkSelfPermission(callerContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(callerContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.i("LocationHelper", "Não tem as permissões necessárias");
                    return;
                }
                locationManager = (LocationManager)  callerContext.getSystemService(Context.LOCATION_SERVICE);
                criteria = new Criteria();
                bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
                Location location = locationManager.getLastKnownLocation(bestProvider);
                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude()) ;
                    ((MainActivity) callerContext).marcarMapa(latLng, "Você está aqui");
                }
                else{
                    //This is what you need:
                    locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
                }

                Log.i("LocationHelper", "Tem as permissões necessárias");
//            }
//        }).start();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i("LocationHelper", "googleApiCliente conectado");
        //teste
//        getLocation();

    }

    @Override
    public void onConnectionSuspended(int i){

    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("LocationHelper", "googleApiCliente falhou ao conectar");
    }

    @Override
    public void onLocationChanged(Location location) {
        //remove location callback:
        Log.i("LocationHelper", "AVISOU!!!!!!!!!!!!!!!!!");
        locationManager.removeUpdates(this);
        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        Handler handler = ((MainActivity)callerContext).handler;
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
                ((MainActivity) callerContext).marcarMapa(latLng, "Você está aqui");
//            }
//        });
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
