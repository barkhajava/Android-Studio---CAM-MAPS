package edu.umb.cs443;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class clustermarker implements ClusterItem {
    private LatLng position;
    private String mTitle ;
    private String mSnippet;

    public clustermarker(LatLng position) {
        this.position = position;
    }


    public void setPosition(LatLng position) {
        this.position = position;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }


}
