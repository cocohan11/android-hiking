//package com.example.iamhere;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//
//import com.naver.maps.map.overlay.InfoWindow;
//
//public class pointAdapter extends InfoWindow.DefaultViewAdapter
//{
//    final String TAG = "pointAdapter";
//    private final Context mContext;
//    private final ViewGroup mParent;
//    public pointAdapter(@NonNull Context context, ViewGroup parent)
//    {
//        super(context);
//        mContext = context;
//        mParent = parent;
//    }
//
//    @NonNull
//    @Override
//    protected View getContentView(@NonNull InfoWindow infoWindow)
//    {
//
//        View view = (View) LayoutInflater.from(mContext).inflate(R.layout.marker_delete, mParent, false);
//
//        TextView sureDelete = (TextView) view.findViewById(R.id.sureDelete);
//        Button button_del = (Button) view.findViewById(R.id.button_del);
//        Button button2_cancel = (Button) view.findViewById(R.id.button2_cancel);
//
//        sureDelete.setText("제주특ㅁ별자치도청");
//        button_del.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e(TAG, " 어댑터에서 눌러짐 ~~");
//            }
//        });
//        //click리스너 여기에 넣으면 되나;;
//
//        return view;
//    }
//}
