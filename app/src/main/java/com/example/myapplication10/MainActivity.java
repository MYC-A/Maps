package com.example.myapplication10;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.oscim.android.MapView;
import org.oscim.core.MapPosition;
import org.oscim.core.Tile;
import org.oscim.layers.tile.buildings.BuildingLayer;
import org.oscim.layers.tile.vector.VectorTileLayer;
import org.oscim.layers.tile.vector.labeling.LabelLayer;
import org.oscim.map.Map;
import org.oscim.theme.VtmThemes;
import org.oscim.tiling.source.mapfile.MapFileTileSource;
import org.oscim.tiling.source.mapfile.MapInfo;
import org.oscim.tiling.source.mapfile.MultiMapFileTileSource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private Map map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initMap();
    }
    private void initMap() {
        MapView mapView = findViewById(R.id.map_view);
        this.map = mapView.map();

        File baseMapFile = getMapFile("chelyab.map");
        MapFileTileSource tileSource = new MapFileTileSource();
        tileSource.setMapFile(baseMapFile.getAbsolutePath());

        VectorTileLayer layer = this.map.setBaseMap(tileSource);

        MapInfo info = tileSource.getMapInfo();
        if (info != null) {
            MapPosition pos = new MapPosition();
            pos.setByBoundingBox(info.boundingBox, Tile.SIZE * 5, Tile.SIZE * 5);
            this.map.setMapPosition(pos);
        }

        this.map.setTheme(VtmThemes.DEFAULT);

        this.map.layers().add(new BuildingLayer(this.map, layer));
        this.map.layers().add(new LabelLayer(this.map, layer));
    }

    private File getMapFile(String mapFileName) {
        File worldMapFile = new File(getFilesDir(), mapFileName);
        if (!worldMapFile.exists()) {
            try(InputStream inputStream = getResources().openRawResource(
                    mapFileName.equals("chelyab.map") ? R.raw.chelyab : R.raw.chelyab)) {
                FileHelper.copyFile(inputStream, worldMapFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return worldMapFile;
    }


}