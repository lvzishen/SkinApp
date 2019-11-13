package com.clean.binder.mgr;

interface ISvcManager{

	void addService(String name,IBinder binder);
	IBinder getService(String name);
}