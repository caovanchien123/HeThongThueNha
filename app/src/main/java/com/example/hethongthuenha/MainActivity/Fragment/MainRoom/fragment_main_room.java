package com.example.hethongthuenha.MainActivity.Fragment.MainRoom;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hethongthuenha.API.PersonAPI;
import com.example.hethongthuenha.ActivityRoomDetail;
import com.example.hethongthuenha.ActivitySearchRoom;
import com.example.hethongthuenha.Adapter.RoomRecyclerView;
import com.example.hethongthuenha.CreateRoom.CreateRoomActivity;
import com.example.hethongthuenha.Model.Comment;
import com.example.hethongthuenha.Model.Room;
import com.example.hethongthuenha.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Date;
import java.util.List;


public class fragment_main_room extends Fragment implements MainRoomContract.View {

    private FloatingActionButton floatingActionButton;
    private RoomRecyclerView adapter;
    private RecyclerView recyclerView;
    private EditText edSearchRoom;
    private MainRoomPresenter presenter;


    public fragment_main_room() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_room, container, false);
        presenter = new MainRoomPresenter(this);
        Init(view);
        Event();
        SearchRoom();
        return view;
    }

    private void SearchRoom() {
        edSearchRoom.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    event != null &&
                            event.getAction() == KeyEvent.ACTION_DOWN &&
                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                if (event == null || !event.isShiftPressed()) {
                    Intent intent = new Intent(getActivity(), ActivitySearchRoom.class);
                    intent.putExtra("search_title", v.getText().toString());
                    startActivity(intent);

                    return true; // consume.
                }
            }
            return false; // pass on to other listeners.
        });
    }

    private void Init(View view) {
        presenter.InitRoom();
        recyclerView = view.findViewById(R.id.roomRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        edSearchRoom = view.findViewById(R.id.etSearch);
    }

    private void Event() {
        floatingActionButton.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), CreateRoomActivity.class)));
    }

    @Override
    public void InitAdapter(List<Room> rooms) {
        adapter = new RoomRecyclerView(getActivity(), rooms);
        recyclerView.setAdapter(adapter);
    }
}