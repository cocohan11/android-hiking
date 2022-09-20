package com.example.iamhere.Model;

import com.naver.maps.map.overlay.Marker;

import java.util.ArrayList;
import java.util.HashMap;

public class Markers_Players {

    ArrayList<ClientInfo> clientList;
    HashMap<String, Marker> thisRoomClientMarkers;

    public Markers_Players(ArrayList<ClientInfo> clientList, HashMap<String, Marker> thisRoomClientMarkers) {
        this.clientList = clientList;
        this.thisRoomClientMarkers = thisRoomClientMarkers;
    }

    public ArrayList<ClientInfo> getClientList() {
        return clientList;
    }

    public HashMap<String, Marker> getThisRoomClientMarkers() {
        return thisRoomClientMarkers;
    }
}
