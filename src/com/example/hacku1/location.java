package com.example.hacku1;

public class location{
	public  double latitude;
	public double longitude;
	public double radius;
	public int ID;
	public void setradius(String rad){
		this.radius=Double.parseDouble(rad);
	}
	public void setlat(String lat){
		this.latitude=Double.parseDouble(lat);
	}
	public  void setlong(String longi){
		this.longitude=Double.parseDouble(longi);
	}
	public double getlat(){
		return this.latitude;
	}
	public double getlong(){
		return this.longitude;
	}
	public int getID(){
		return this.ID;
	}
}

