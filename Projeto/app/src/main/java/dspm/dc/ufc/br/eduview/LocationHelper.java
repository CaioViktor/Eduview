package dspm.dc.ufc.br.eduview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class LocationHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private Context callerContext;

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

    public LatLng getLocation() {
        
        if (ActivityCompat.checkSelfPermission(callerContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(callerContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("LocationHelper", "Não tem as permissões necessárias");
            return null;
        }


        Log.i("LocationHelper", "Tem as permissões necessárias");
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        Log.i("LocationHelper", "Last Location: " + location.getLatitude() + " - " + location.getLongitude());
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i("LocationHelper", "googleApiCliente conectado");
        //teste
        getLocation();

    }

    @Override
    public void onConnectionSuspended(int i){

    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("LocationHelper", "googleApiCliente falhou ao conectar");
    }
}
