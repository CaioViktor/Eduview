package dspm.dc.ufc.br.eduview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,ObserverServer {
    private GoogleMap map;
    private LatLng defaultLocal = new LatLng(-3.7460927, -38.5743825);
    private int defaultZoom = 16;
    public Handler handler = new Handler();
    private Server server;
    private LatLng posicao;
    private ArrayList<Escola> escolas;
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
        server = new Server(this);
        server.attachObserver(this);//Increve o objeto para ser notificado
        posicao = null;
        escolas = new ArrayList<>();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //mapinha carregado e feliz
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //checa se consegue ler a localizacao
        final MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
            @Override
            public void gotLocation(Location location){
                //Usar a localizacao aqui!

                final LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        setarPosicao(latLng);
                    }
                });

            }
        };

        final MyLocation localizacao = new MyLocation(handler);
        new Thread(new Runnable() {
            @Override
            public void run() {
                localizacao.getLocation(MainActivity.this, locationResult);
            }
        }).start();
    }
    public void setarPosicao(LatLng latLng){
        posicao = latLng;
        marcarMapa(posicao, "Você está aqui");
        JSONObject object = new JSONObject();
        //Exemplo de requisição com filtro
//        try {
//            object.put("where","UPPER(nome) LIKE \'%ACA%\'");
//            server.POST(server.HTTP + server.HOST + server.PORT + "/listescola/" + posicao.longitude + "/" + posicao.latitude + "/10/10", object.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        server.POST(server.HTTP + server.HOST + server.PORT + "/listescola/" + posicao.latitude + "/" + posicao.longitude+ "/10/10", null);
    }
    public void marcarMapa(LatLng posicao,String texto){
        LatLng local = posicao;
        int zoom;
        //checa se consegue ler a localizacao

        //LatLng localRetornado = lh.getLocation();
        //se nao conseguir, coloca no default (Pici)

        zoom = defaultZoom;
        //move a camera pro local do usuario/default
        CameraPosition cameraPosition = new CameraPosition.Builder().target(local).zoom(zoom).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //adiciona um marker de onde o usuario esta/default:
        MarkerOptions marker = new MarkerOptions().position(local).title(texto);
        map.addMarker(marker);
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
    public void notifyPOST(String response) {
        try{
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.has("codigo") && jsonObject.getInt("codigo") == 1)
                Toast.makeText(this,"Ocorreu um erro interno",Toast.LENGTH_LONG);
            else{
                int size = jsonObject.getInt("contador");
                for(int i = 1; i <= size; i++){
                    Escola escola = new Escola(jsonObject.getJSONObject(i+"").toString());
                    escolas.add(escola);
                }

            }
        }catch(Exception e){
            e.printStackTrace();
        }
        for(Escola e : escolas)
            Log.i("LOGP",e.getBairro());
    }

    @Override
    public void notifyGET(String response) {

    }

    @Override
    public Handler getHandler() {
        return handler;
    }
}
