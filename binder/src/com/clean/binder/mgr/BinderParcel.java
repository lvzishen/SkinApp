package com.clean.binder.mgr;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

public class BinderParcel implements Parcelable{

	public static final Parcelable.Creator<BinderParcel> CREATOR = new Parcelable.Creator<BinderParcel>(){
		@Override
		public BinderParcel createFromParcel(Parcel arg0) {
			return readFromParcel(arg0);
		}

		@Override
		public BinderParcel[] newArray(int arg0) {
			return new BinderParcel[arg0] ;
		}
		
	};
	@Override
	public int describeContents() {
		return 0;
	}

	public IBinder mProxy = null;
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeStrongBinder(mProxy);
	}
	
	public static BinderParcel readFromParcel(Parcel arg0){
		BinderParcel bp = new BinderParcel();
		bp.mProxy = arg0.readStrongBinder();
		return bp;
	}

}
