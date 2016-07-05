package dspm.dc.ufc.br.eduview;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.ExceptionParser;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {//},ObserverServer {
    private GoogleMap map;

    private int defaultZoom = 14;
    public Handler handler = new Handler();
    private Server server;
    private LatLng posicao;
    private ArrayList<Escola> escolas;
    private LocationHelper lh;
    private ServerCallsHelper serverCallsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Gerado automaticamente
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Apaguei o fab (botaozinho que fica no canto)

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //termina o gerado automaticamente

        SupportMapFragment map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        map.getMapAsync(this);

        serverCallsHelper = new ServerCallsHelper(this);
        posicao = null;

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //mapinha carregado e feliz
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //o proximo metodo é pra permitir que o marker exiba mais que duas linhas
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                Context context = getApplicationContext(); //or getActivity(), YourActivity.this, etc.

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        lh = new LocationHelper(this);
        lh.conect(); //chamar o connect vai fazer a chamada de pegar localizacao

    }

    public void setarPosicao(LatLng latLng){
        map.clear();
        Log.i("MainActivity","Entrou no setarPosicao");
        posicao = latLng;
        marcarMapa(posicao,getResources().getString(R.string.MAIN_MARKER_TEXT));

        //Exemplo de requisição com filtro
        serverCallsHelper.getEscolas(posicao,10,15);

    }

    public void marcarMapa(LatLng posicao,String texto){

        //move a camera pro local do usuario/default
        CameraPosition cameraPosition = new CameraPosition.Builder().target(posicao).zoom(defaultZoom).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //adiciona um marker de onde o usuario esta/default:
        MarkerOptions marker = new MarkerOptions().position(posicao).title(texto);
        map.addMarker(marker);
    }

    public void marcarEscolaNoMapa(Escola e){
        try{
            float lat = Float.parseFloat(e.getLatitude());
            float lng = Float.parseFloat(e.getLongitude());

            Log.i("MainActivity","Vai marcar escola de coordenadas: "+lng+" e "+lat);
            String titulo = e.getNome();
            String snippet = "Escola da rede "+e.getRede()+"\nEndereco: "+e.getRua()+" - "+e.getNumero();

            MarkerOptions marker = new MarkerOptions().position(new LatLng(lng,lat)).icon(BitmapDescriptorFactory.fromResource(R.drawable.schoolmarker)).title(titulo).snippet(snippet);
            map.addMarker(marker);

        }catch(Exception exception){

        }



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        if (requestCode == LocationHelper.REQUEST_LOCATION) {
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
                lh.startListeningUpdates();
                //lh.getLocationAndMark();

            } else {
                // Permission was denied or request was cancelled
                Log.i("RequestGPS","Permissao Negada");
                (Toast.makeText(this,getResources().getString(R.string.PERMISSION_DENIED), Toast.LENGTH_LONG)).show();
                lh.getDefaultLocationAndMark();
            }
        }
    }

    public boolean requestGPSPermission(){
        Log.i("RequestGPS","Entrou no requestGPSPermission");
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LocationHelper.REQUEST_LOCATION);

        return false;
    }
}
