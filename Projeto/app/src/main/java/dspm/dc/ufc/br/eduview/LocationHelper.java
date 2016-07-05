package dspm.dc.ufc.br.eduview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.Semaphore;

public class LocationHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleApiClient googleApiClient;
    private Context callerContext;
    private Criteria criteria;
    private String bestProvider;
    private LocationManager locationManager;
    LocationRequest mLocationRequest;

    public static LatLng defaultLocal = new LatLng(-3.7460927, -38.5743825);

    public LocationHelper(Context context) {
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(context);
        callerContext = context;
        builder.addApi(LocationServices.API);
        builder.addConnectionCallbacks(this);
        builder.addOnConnectionFailedListener(this);
        googleApiClient = builder.build();
    }

    public void conect() {
        googleApiClient.connect();
    }

    public void disconnect() {
        googleApiClient.disconnect();
    }

    static public int REQUEST_LOCATION = 1;

    @Override
    public void onConnected(Bundle bundle) {
        Log.i("LocationHelper", "googleApiCliente conectado");

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        Log.i("LocationHelper", "Versão da API: " + String.valueOf(currentapiVersion));

        if (currentapiVersion >= 23) {
            //Precisa de permissao em tempo de execucao
            if (ActivityCompat.checkSelfPermission(callerContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Check Permissions Now
                Log.i("LocationHelper", "Não possui permissões");
                Log.i("LocationHelper", "Vai chamar método MainActivity.requestGPSPermission");
                ((MainActivity) callerContext).requestGPSPermission();

            } else {
                // permission has been granted, continue as usual
                startListeningUpdates();
            }

        } else {
            //API < 23, Nao precisa das permissoes
            startListeningUpdates();
        }
    }

    public boolean isLocationEnabled() {
        //codigo de http://stackoverflow.com/questions/10311834/how-to-check-if-location-services-are-enabled
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(callerContext.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(callerContext.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public void startListeningUpdates() {

        if (ActivityCompat.checkSelfPermission(callerContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(callerContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager = (LocationManager) callerContext.getSystemService(Context.LOCATION_SERVICE);
        if(locationManager==null) {
            getDefaultLocationAndMark();
            return;
        }

        if(!isLocationEnabled()){
            (Toast.makeText(callerContext,callerContext.getResources().getString(R.string.GPS_DISABLED), Toast.LENGTH_LONG)).show();
            getDefaultLocationAndMark();
        }

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);
    }

    public void getDefaultLocationAndMark(){
        Log.i("LocationHelper","Vai usar a localizacao Padrao (Campus do Pici)"+String.valueOf(defaultLocal.latitude)+" - "+String.valueOf(defaultLocal.longitude));
        ((MainActivity)callerContext).setarPosicao(defaultLocal);
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
        Log.i("LocationHelper", "Evento de onLocationChanged");
        ((MainActivity)callerContext).setarPosicao(new LatLng(location.getLatitude(),location.getLongitude()));
        Log.i("LocationHelper","Nova localizacao: "+location.getLatitude()+" - "+location.getLongitude());
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }
}
