package com.example.dos_thesis.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.dos_thesis.R;
import com.example.dos_thesis.adapter.LocationRecyclerViewAdapter;
import com.example.dos_thesis.model.IndividualLocation;
import com.example.dos_thesis.util.LinearLayoutManagerWithSmoothScroller;
import com.google.firebase.database.annotations.Nullable;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.turf.TurfConstants;
import com.mapbox.turf.TurfConversion;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.expressions.Expression.eq;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

/**
 * Activity with a Mapbox map and recyclerview to view various locations
 */
public class MapActivity extends AppCompatActivity implements
  LocationRecyclerViewAdapter.ClickListener, MapboxMap.OnMapClickListener {


  private static final LatLngBounds LOCKED_MAP_CAMERA_BOUNDS = new LatLngBounds.Builder()
          .include(new LatLng(7.294, 125.4207))
          .include(new LatLng(7.0491, 125.6464))
          .build();
  private static final LatLng DEVICE_LOCATION_LAT_LNG = new LatLng(7.0725, 125.6127);
  private static final int MAPBOX_LOGO_OPACITY = 75;
  private static final int CAMERA_MOVEMENT_SPEED_IN_MILSECS = 1200;
  private static final float NAVIGATION_LINE_WIDTH = 9;
  private static final String PROPERTY_SELECTED = "selected";
  private DirectionsRoute currentRoute;
  private FeatureCollection featureCollection;
  private MapboxMap mapboxMap;
  private MapView mapView;
  private RecyclerView locationsRecyclerView;
  private ArrayList<IndividualLocation> listOfIndividualLocations;
  private CustomThemeManager customThemeManager;
  private LocationRecyclerViewAdapter styleRvAdapter;
  private int chosenTheme;
  private String TAG = "MapActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Mapbox.getInstance(this, getString( R.string.access_token));

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    setContentView(R.layout.activity_map);

    try {
      getFeatureCollectionFromJson();
    } catch (Exception exception) {
      Log.e("MapActivity", "onCreate: " + exception);
      Toast.makeText(this, R.string.failure_to_load_file, Toast.LENGTH_LONG).show();
    }

    listOfIndividualLocations = new ArrayList<>();

    chosenTheme = R.style.AppTheme_Neutral;

    mapView = findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(new OnMapReadyCallback() {
      @Override
      public void onMapReady(final MapboxMap mapboxMap) {

        customThemeManager = new CustomThemeManager(chosenTheme, MapActivity.this);

        mapboxMap.setStyle(new Style.Builder().fromUrl(customThemeManager.getMapStyle()), new Style.OnStyleLoaded() {
          @Override
          public void onStyleLoaded(@NonNull Style style) {

            MapActivity.this.mapboxMap = mapboxMap;

            ImageView logo = mapView.findViewById(R.id.logoView);
            logo.setAlpha(MAPBOX_LOGO_OPACITY);

            // Set bounds for the map camera so that the user can't pan the map outside of the Davao area
            mapboxMap.setLatLngBoundsForCameraTarget(LOCKED_MAP_CAMERA_BOUNDS);

            initStoreLocationIconSymbolLayer();

            initSelectedStoreSymbolLayer();

            initNavigationPolylineLineLayer();

            List<Feature> featureList = featureCollection.features();

            GeoJsonSource source = mapboxMap.getStyle().getSourceAs("store-location-source-id");
            if (source != null) {
              source.setGeoJson(FeatureCollection.fromFeatures(featureList));
            }

            if (featureList != null) {

              for (int x = 0; x < featureList.size(); x++) {

                Feature singleLocation = featureList.get(x);

                String singleLocationName = singleLocation.getStringProperty("name");
                String singleLocationDescription = singleLocation.getStringProperty("description");

                singleLocation.addBooleanProperty(PROPERTY_SELECTED, false);

                Point singleLocationPosition = (Point) singleLocation.geometry();

                LatLng singleLocationLatLng = new LatLng(singleLocationPosition.latitude(),
                  singleLocationPosition.longitude());

                listOfIndividualLocations.add(new IndividualLocation(
                  singleLocationName,
                  singleLocationDescription,
                  singleLocationLatLng

                ));

                getInformationFromDirectionsApi(singleLocationPosition, false, x);
              }

              addDeviceLocationMarkerToMap();

              setUpRecyclerViewOfLocationCards(chosenTheme);

              mapboxMap.addOnMapClickListener(MapActivity.this);

              Toast.makeText(MapActivity.this, "Click on a card", Toast.LENGTH_SHORT).show();
            }
          }

        });

      }
    });
  }


  @Override
  public boolean onMapClick(@NonNull LatLng point) {
    handleClickIcon(mapboxMap.getProjection().toScreenLocation(point));
    return true;
  }

  private boolean handleClickIcon(PointF screenPoint) {
    List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint, "store-location-layer-id");
    if (!features.isEmpty()) {
      String name = features.get(0).getStringProperty("name");
      List<Feature> featureList = featureCollection.features();
      for (int i = 0; i < featureList.size(); i++) {

        if (featureList.get(i).getStringProperty("name").equals(name)) {
          Point selectedFeaturePoint = (Point) featureList.get(i).geometry();

          if (featureSelectStatus(i)) {
            setFeatureSelectState(featureList.get(i), false);
          } else {
            setSelected(i);
          }
          if (selectedFeaturePoint.latitude() != DEVICE_LOCATION_LAT_LNG.getLatitude()) {
            for (int x = 0; x < featureCollection.features().size(); x++) {

              if (listOfIndividualLocations.get(x).getLocation().getLatitude() == selectedFeaturePoint.latitude()) {

                locationsRecyclerView.smoothScrollToPosition(x);
              }
            }
          }
        } else {
          setFeatureSelectState(featureList.get(i), false);
        }
      }
      return true;
    } else {
      return false;
    }
  }

  /**
   * The LocationRecyclerViewAdapter's interface which listens to clicks on each location's card
   *
   * @param position the clicked card's position/index in the overall list of cards
   */
  @Override
  public void onItemClick(int position) {
    IndividualLocation selectedLocation = listOfIndividualLocations.get(position);


    List<Feature> featureList = featureCollection.features();
    Point selectedLocationPoint = (Point) featureCollection.features().get(position).geometry();
    for (int i = 0; i < featureList.size(); i++) {
      if (featureList.get(i).getStringProperty("name").equals(selectedLocation.getName())) {
        if (featureSelectStatus(i)) {
          setFeatureSelectState(featureList.get(i), false);
        } else {
          setSelected(i);
        }
      } else {
        setFeatureSelectState(featureList.get(i), false);
      }
    }

    if (selectedLocation != null) {
      repositionMapCamera(selectedLocationPoint);
    }

    if (deviceHasInternetConnection()) {
      // Start call to the Mapbox Directions API
      if (selectedLocation != null) {
        getInformationFromDirectionsApi(selectedLocationPoint, true, null);
      }
    } else {
      Toast.makeText(this, R.string.no_internet_message, Toast.LENGTH_LONG).show();
    }
  }


  private void initStoreLocationIconSymbolLayer() {
    Style style = mapboxMap.getStyle();
    if (style != null) {
      // Add the icon image to the map
      style.addImage("store-location-icon-id", customThemeManager.getUnselectedMarkerIcon());

      // Create and add the GeoJsonSource to the map
      GeoJsonSource storeLocationGeoJsonSource = new GeoJsonSource("store-location-source-id");
      style.addSource(storeLocationGeoJsonSource);

      // Create and add the store location icon SymbolLayer to the map
      SymbolLayer storeLocationSymbolLayer = new SymbolLayer("store-location-layer-id",
        "store-location-source-id");
      storeLocationSymbolLayer.withProperties(
        iconImage("store-location-icon-id"),
        iconAllowOverlap(true),
        iconIgnorePlacement(true)
      );
      style.addLayer(storeLocationSymbolLayer);

    } else {
      Log.d("StoreFinderActivity", "initStoreLocationIconSymbolLayer: Style isn't ready yet.");

      throw new IllegalStateException("Style isn't ready yet.");
    }
  }


  private void initSelectedStoreSymbolLayer() {
    Style style = mapboxMap.getStyle();
    if (style != null) {

      // Add the icon image to the map
      style.addImage("selected-store-location-icon-id", customThemeManager.getSelectedMarkerIcon());

      // Create and add the store location icon SymbolLayer to the map
      SymbolLayer selectedStoreLocationSymbolLayer = new SymbolLayer("selected-store-location-layer-id",
        "store-location-source-id");
      selectedStoreLocationSymbolLayer.withProperties(
        iconImage("selected-store-location-icon-id"),
        iconAllowOverlap(true)
      );
      selectedStoreLocationSymbolLayer.withFilter(eq((get(PROPERTY_SELECTED)), literal(true)));
      style.addLayer(selectedStoreLocationSymbolLayer);
    } else {
      Log.d("StoreFinderActivity", "initSelectedStoreSymbolLayer: Style isn't ready yet.");
      throw new IllegalStateException("Style isn't ready yet.");
    }
  }

  /**
   * Checks whether a Feature's boolean "selected" property is true or false
   *
   * @param index the specific Feature's index position in the FeatureCollection's list of Features.
   * @return true if "selected" is true. False if the boolean property is false.
   */
  private boolean featureSelectStatus(int index) {
    if (featureCollection == null) {
      return false;
    }
    return featureCollection.features().get(index).getBooleanProperty(PROPERTY_SELECTED);
  }

  /**
   * Set a feature selected state.
   *
   * @param index the index of selected feature
   */
  private void setSelected(int index) {
    Feature feature = featureCollection.features().get(index);
    setFeatureSelectState(feature, true);
    refreshSource();
  }

  /**
   * Selects the state of a feature
   *
   * @param feature the feature to be selected.
   */
  private void setFeatureSelectState(Feature feature, boolean selectedState) {
    feature.properties().addProperty(PROPERTY_SELECTED, selectedState);
    refreshSource();
  }

  /**
   * Updates the display of data on the map after the FeatureCollection has been modified
   */
  private void refreshSource() {
    GeoJsonSource source = mapboxMap.getStyle().getSourceAs("store-location-source-id");
    if (source != null && featureCollection != null) {
      source.setGeoJson(featureCollection);
    }
  }

  private void getInformationFromDirectionsApi(Point destinationPoint,
                                               final boolean fromMarkerClick, @Nullable final Integer listIndex) {
    // Set up origin and destination coordinates for the call to the Mapbox Directions API
    Point CurrentLocation = Point.fromLngLat(DEVICE_LOCATION_LAT_LNG.getLongitude(),
      DEVICE_LOCATION_LAT_LNG.getLatitude());

    Point destinationMarker = Point.fromLngLat(destinationPoint.longitude(), destinationPoint.latitude());

    // Initialize the directionsApiClient object for eventually drawing a navigation route on the map
    MapboxDirections directionsApiClient = MapboxDirections.builder()
      .origin(CurrentLocation)
      .destination(destinationMarker)
      .overview(DirectionsCriteria.OVERVIEW_FULL)
      .profile(DirectionsCriteria.PROFILE_CYCLING) //default 'DRIVING' changed to 'CYCLING'
      .accessToken(getString(R.string.access_token))
      .build();

    directionsApiClient.enqueueCall(new Callback<DirectionsResponse>() {
      @Override
      public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
        // Check that the response isn't null and that the response has a route
        if (response.body() == null) {
          Log.e("MapActivity", "No routes found, make sure you set the right user and access token.");
        } else if (response.body().routes().size() < 1) {
          Log.e("MapActivity", "No routes found");
        } else {
          if (fromMarkerClick) {
            // Retrieve and draw the navigation route on the map
            currentRoute = response.body().routes().get(0);
            drawNavigationPolylineRoute(currentRoute);
          } else {
            // Use Mapbox Turf helper method to convert meters to miles and then format the mileage number
            DecimalFormat df = new DecimalFormat("#.#");
            String finalConvertedFormattedDistance = String.valueOf(df.format(TurfConversion.convertLength(
              response.body().routes().get(0).distance(), TurfConstants.UNIT_METERS,
              TurfConstants.UNIT_MILES)));

            // Set the distance for each location object in the list of locations
            if (listIndex != null) {
              listOfIndividualLocations.get(listIndex).setDistance(finalConvertedFormattedDistance);
              // Refresh the displayed recyclerview when the location's distance is set
              styleRvAdapter.notifyDataSetChanged();
            }
          }
        }
      }

      @Override
      public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
        Toast.makeText(MapActivity.this, R.string.failure_to_retrieve, Toast.LENGTH_LONG).show();
      }
    });
  }

  private void repositionMapCamera(Point newTarget) {
    CameraPosition newCameraPosition = new CameraPosition.Builder()
      .target(new LatLng(newTarget.latitude(), newTarget.longitude()))
      .build();
    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition), CAMERA_MOVEMENT_SPEED_IN_MILSECS);
  }

  private void addDeviceLocationMarkerToMap() {
    // Add the fake user location marker to the map
    Style style = mapboxMap.getStyle();
    if (style != null) {
      // Add the icon image to the map
      style.addImage("device-location-icon-id", customThemeManager.getLocationIcon());

      style.addSource(new GeoJsonSource("device-location-source-id", Feature.fromGeometry(
        Point.fromLngLat(DEVICE_LOCATION_LAT_LNG.getLongitude(), DEVICE_LOCATION_LAT_LNG.getLatitude()))));

      style.addLayer(new SymbolLayer("device-location-layer-id",
        "device-location-source-id").withProperties(
        iconImage("device-location-icon-id"),
        iconAllowOverlap(true),
        iconIgnorePlacement(true)
      ));
    } else {
      throw new IllegalStateException("Style isn't ready yet.");
    }
  }

  private void getFeatureCollectionFromJson() throws IOException {
    try {
// Use fromJson() method to convert the GeoJSON file into a usable FeatureCollection object
      featureCollection = FeatureCollection.fromJson(loadGeoJsonFromAsset("list_of_locations.geojson"));

    } catch (Exception exception) {
      Log.e("MapActivity", "getFeatureCollectionFromJson: " + exception);
    }
  }

  private String loadGeoJsonFromAsset(String filename) {
    try {
      // Load the GeoJSON file from the local asset folder
      InputStream is = getAssets().open(filename);
      int size = is.available();
      byte[] buffer = new byte[size];
      is.read(buffer);
      is.close();
      return new String(buffer, "UTF-8");
    } catch (Exception exception) {
      Log.e("MapActivity", "Exception Loading GeoJSON: " + exception.toString());
      exception.printStackTrace();
      return null;
    }
  }

  private void setUpRecyclerViewOfLocationCards(int chosenTheme) {

    locationsRecyclerView = findViewById(R.id.map_layout_rv);
    locationsRecyclerView.setHasFixedSize(true);
    locationsRecyclerView.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(this));
    styleRvAdapter = new LocationRecyclerViewAdapter(listOfIndividualLocations,
      getApplicationContext(), this, chosenTheme);
    locationsRecyclerView.setAdapter(styleRvAdapter);
    SnapHelper snapHelper = new LinearSnapHelper();
    snapHelper.attachToRecyclerView(locationsRecyclerView);
  }

  private void drawNavigationPolylineRoute(DirectionsRoute route) {
    // Retrieve and update the source designated for showing the store location icons
    GeoJsonSource source = mapboxMap.getStyle().getSourceAs("navigation-route-source-id");
    if (source != null) {
      source.setGeoJson(FeatureCollection.fromFeature(Feature.fromGeometry(
        LineString.fromPolyline(route.geometry(), PRECISION_6))));
    }
  }

  private void initNavigationPolylineLineLayer() {
    GeoJsonSource navigationLineLayerGeoJsonSource = new GeoJsonSource("navigation-route-source-id");
    mapboxMap.getStyle().addSource(navigationLineLayerGeoJsonSource);

    LineLayer navigationRouteLineLayer = new LineLayer("navigation-route-layer-id",
      navigationLineLayerGeoJsonSource.getId());
    navigationRouteLineLayer.withProperties(
      lineColor(customThemeManager.getNavigationLineColor()),
      lineWidth(NAVIGATION_LINE_WIDTH)
    );
    mapboxMap.getStyle().addLayerBelow(navigationRouteLineLayer, "store-location-layer-id");
  }

  // Add the mapView's lifecycle to the activity's lifecycle methods
  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // End the navigation session
    mapView.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  private boolean deviceHasInternetConnection() {
    ConnectivityManager connectivityManager = (ConnectivityManager)
      getApplicationContext().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
    return activeNetwork != null && activeNetwork.isConnected();
  }

  /**
   * Custom class which creates marker icons and colors based on the selected theme
   */
  class CustomThemeManager {
    private int selectedTheme;
    private Context context;
    private Bitmap unselectedMarkerIcon;
    private Bitmap selectedMarkerIcon;
    private Bitmap LocationIcon;
    private int navigationLineColor;
    private String mapStyle;

    CustomThemeManager(int selectedTheme, Context context) {
      this.selectedTheme = selectedTheme;
      this.context = context;
      initializeTheme();
    }

    private void initializeTheme() {
      switch (selectedTheme) {
        case R.style.AppTheme_Blue:
          mapStyle = getString(R.string.blue_map_style);
          navigationLineColor = getResources().getColor(R.color.navigationRouteLine_blue);
          unselectedMarkerIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.blue_unselected_ice_cream);
          selectedMarkerIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.blue_selected_ice_cream);
          LocationIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.blue_user_location);
          break;
        case R.style.AppTheme_Neutral:
          mapStyle = Style.MAPBOX_STREETS;
          navigationLineColor = getResources().getColor(R.color.navigationRouteLine_neutral);
          unselectedMarkerIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.house_icon);
          selectedMarkerIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.house_icon);
          LocationIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.neutral_orange_user_location);
          break;
      }
    }

    public Bitmap getUnselectedMarkerIcon() {
      return unselectedMarkerIcon;
    }

    public Bitmap getLocationIcon() {
      return LocationIcon;
    }

    public Bitmap getSelectedMarkerIcon() {
      return selectedMarkerIcon;
    }

    int getNavigationLineColor() {
      return navigationLineColor;
    }

    public String getMapStyle() {
      return mapStyle;
    }
  }
}
