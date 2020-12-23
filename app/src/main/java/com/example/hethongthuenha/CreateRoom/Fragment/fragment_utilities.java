package com.example.hethongthuenha.CreateRoom.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.hethongthuenha.CreateRoom.CreateRoomActivity;
import com.example.hethongthuenha.MainActivity.MainActivity;
import com.example.hethongthuenha.Model.Room;
import com.example.hethongthuenha.Model.Utilities_Room;
import com.example.hethongthuenha.R;

import java.util.ArrayList;
import java.util.List;


public class fragment_utilities extends Fragment {

    private final CheckBox[] cbUtilities = new CheckBox[9];
    private List<String> utilities;
    private IDataCommunication dataCommunication;

    public fragment_utilities() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dataCommunication = (IDataCommunication) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement DataCommunication");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_utilities, container, false);
        utilities = new ArrayList<>();
        Button btnFinishStage4 = view.findViewById(R.id.btnFinishStage4);
        for (int i = 0; i < 5; i++) {
            String checkBoxStageId = "cbUtility" + (i + 1);
            int resId = getResources().getIdentifier(checkBoxStageId, "id", getActivity().getPackageName());
            cbUtilities[i] = view.findViewById(resId);
        }

        if (CreateRoomActivity.roomExist != null) {
            Room room = CreateRoomActivity.roomExist;

            for (String utilities : room.getStage4().getDescription_utility())
                for (int i = 0; i < 5; i++)
                    if (utilities.equals(cbUtilities[i].getText().toString()))
                        cbUtilities[i].setChecked(true);

        }


        btnFinishStage4.setOnClickListener(v -> {
            clickUtilities();
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        });

        return view;
    }

    private void clickUtilities() {
        for (int i = 0; i < 5; i++) {
            if (cbUtilities[i].isChecked())
                utilities.add(cbUtilities[i].getText().toString());
        }
        dataCommunication.Utilities(new Utilities_Room(utilities));
    }
}