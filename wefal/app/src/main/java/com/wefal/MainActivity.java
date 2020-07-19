package com.wefal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();
    // fragments
    private TradeListActivity trade_list = new TradeListActivity(); // menu 1
    private AddTradeActivity add_trade = new AddTradeActivity();    // menu 2
    private ChatListActivity chat_list = new ChatListActivity();    // menu 3
    private MypageActivity mypage = new MypageActivity();           // menu 4

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        // first screen
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, trade_list).commitAllowingStateLoss();

        // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.trade_list: {
                        transaction.replace(R.id.frame_layout, trade_list).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.add_trade: {
                        transaction.replace(R.id.frame_layout, add_trade).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.chat_list: {
                        transaction.replace(R.id.frame_layout, chat_list).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.mypage: {
                        transaction.replace(R.id.frame_layout, mypage).commitAllowingStateLoss();
                        break;
                    }
                }

                return true;
            }
        });
    }
}
