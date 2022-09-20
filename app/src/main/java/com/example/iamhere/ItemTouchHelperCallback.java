package com.example.iamhere;

import static com.example.iamhere.L_login.myEmail;
import static com.example.iamhere.M_share_2_Map.retrofit객체;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iamhere.Interface.ItemTouchHelperListener;
import com.example.iamhere.Interface.Sharing;
import com.example.iamhere.Model.Sharing_room;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

enum ButtonsState{ //?
    GONE,
    LEFT_VISIBLE,
    RIGHT_VISIBLE
}

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {


    String TAG = "ItemTouchHelperCallback";
    private ItemTouchHelperListener listener;
    private boolean swipeBack = false; //삭제하려다가 말았냐
    private ButtonsState buttonsShowedState = ButtonsState.GONE;
    private final float buttonWidth = 200;
    private RectF buttonInstance = null;
    private RecyclerView.ViewHolder currenrtItemViewHolder = null;


    public ItemTouchHelperCallback(ItemTouchHelperListener listener) {
        this.listener = listener;
    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int drag_flags = ItemTouchHelper.UP|ItemTouchHelper.DOWN;
        int swipe_flags = ItemTouchHelper.START;
//        int swipe_flags = ItemTouchHelper.START|ItemTouchHelper.END; //이동(방향향 플래그를 만드는 방법
        return makeMovementFlags(drag_flags,swipe_flags);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

        return listener.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onItemSwipe(viewHolder.getAdapterPosition());
        listener.onItemMove(0, ItemTouchHelper.LEFT); //왼쪽으로만! 오른쪽이 계속 움직임
    }

    //아이템을 터치하거나 스와이프하거나 뷰에 변화가 생길경우 불러오는 함수
    @SuppressLint("LongLogTag")
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        //아이템이 스와이프 됐을경우 버튼을 그려주기 위해서 스와이프가 됐는지 확인
        Log.e(TAG,
                "onChildDraw() \n actionState : "+actionState +
                "\n dX : "+ dX +
                "\n dY : "+ dY +
                "\n isCurrentlyActive : "+ isCurrentlyActive
        );


        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            if(buttonsShowedState != ButtonsState.GONE){

                Log.e(TAG,"버튼 완전히 있다 ");
                if(buttonsShowedState == ButtonsState.RIGHT_VISIBLE) dX = Math.min(dX, -buttonWidth);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                //다른 곳 클릭해서 버튼을 숨긴다
                //여기가 아닌가


            }else{
                Log.e(TAG,"버튼 없다 ");
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            if(buttonsShowedState == ButtonsState.GONE){ // 왜 또 있는거지 >> 이동을 부드럽게 해줌
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                Log.e(TAG,"버튼 없다2 ");
            }
        }
        currenrtItemViewHolder = viewHolder;

        //버튼을 그려주는 함수
        drawButtons(c, currenrtItemViewHolder);

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {

        Log.e(TAG,"convertToAbsoluteDirection() " +
                "\n flags : "+ flags +
                "\n layoutDirection : "+ layoutDirection +
                "\n swipeBack : "+ swipeBack
              );

        if(swipeBack){
            swipeBack = false;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    // 셋터치하는 순간에 계속
    private void setTouchListener(final Canvas c, final RecyclerView recyclerView,
                                  final RecyclerView.ViewHolder viewHolder,
                                  final float dX, final float dY, final int actionState,
                                  final boolean isCurrentlyActive){
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint({"ClickableViewAccessibility", "LongLogTag"})
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.e(TAG,"setTouchListener - onTouch() dX : "+String.valueOf(dX));

                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                if(swipeBack){

                    if(dX < -buttonWidth) buttonsShowedState = ButtonsState.RIGHT_VISIBLE;

                    if(buttonsShowedState != ButtonsState.GONE){
                        setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        setItemsClickable(recyclerView, false);
                    }
                }
                return false;
            }
        });
    }


    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder){

        Log.e("drawButtons() c : ", String.valueOf(c));

        float buttonWidthWithOutPadding = buttonWidth - 2; //삭제버튼 패딩
        float corners = 5;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        buttonInstance = null;

        //왼쪽으로 스와이프 했을때 (오른쪽에 버튼이 보여지게 될 경우)
        if(buttonsShowedState == ButtonsState.RIGHT_VISIBLE){
            RectF rightButton = new RectF(itemView.getRight() - buttonWidthWithOutPadding, itemView.getTop() + 2, itemView.getRight() -2,
                    itemView.getBottom() - 2);
            p.setColor(Color.RED);
            c.drawRoundRect(rightButton, corners, corners, p);
            drawText("삭제", c, rightButton, p);

            buttonInstance = rightButton;
        }
    }

    //버튼의 텍스트 그려주기
    private void drawText(String text, Canvas c, RectF button, Paint p){

        float textSize = 35;
        p.setColor(Color.WHITE);
        p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD)); //두껍게 해야 잘 보일 듯
        p.setAntiAlias(true);
        p.setTextSize(textSize);

        float textWidth = p.measureText(text);
        c.drawText(text, button.centerX() - (textWidth/2), button.centerY() + (textSize/2), p);
    }

    // 삭제되고 나서 다운..? 버튼 안 보이게 되는 곳
    private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView
            , final RecyclerView.ViewHolder viewHolder, final float dX, final float dY
            , final int actionState, final boolean isCurrentlyActive){
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("LongLogTag")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.e(TAG,"setTouchDownListener() - onTouch() : event"+String.valueOf(event));

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
                return false;
            }
        });
    }


    // 삭제되고나서 위로 이동
    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView
            , final RecyclerView.ViewHolder viewHolder, final float dX, final float dY
            , final int actionState, final boolean isCurrentlyActive){

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("LongLogTag")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.e(TAG,"setTouchUpListener() - onTouch() : event"+String.valueOf(event));

                ItemTouchHelperCallback.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
                recyclerView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Log.e(TAG,"recyclerView.setOnTouchListener"); //?

                        return false;
                    }
                });

                setItemsClickable(recyclerView, true);
                swipeBack = false;

                if(listener != null && buttonInstance != null && buttonInstance.contains(event.getX(), event.getY())){
                    if(buttonsShowedState == ButtonsState.RIGHT_VISIBLE){

                        Log.e(TAG,"삭제되는 곳인가");
                        listener.onRightClick(viewHolder.getAdapterPosition(), viewHolder);

                    }
                } else {
                    Log.e(TAG,"삭제되는 곳이 아니라면..? viewHolder.getAdapterPosition() :"+viewHolder.getAdapterPosition());
                }



                buttonsShowedState = ButtonsState.GONE;
                currenrtItemViewHolder = null;
                return false;
            }
        });
    }



    //리사이클러뷰 아이템이 클릭 가능한 상태이면 true, 버튼때문에 사라지면 false
   @SuppressLint("LongLogTag")
    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable){

        Log.e(TAG,"setItemsClickable() isClickable :"+String.valueOf(isClickable));

        for(int i = 0; i < recyclerView.getChildCount(); i++){
            recyclerView.getChildAt(i).setClickable(isClickable);
        }

    }


}
