package com.example.hethongthuenha.MainActivity.Fragment.Requiment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hethongthuenha.API.PersonAPI;
import com.example.hethongthuenha.ActivityChat;
import com.example.hethongthuenha.ActivityRoomDetail;
import com.example.hethongthuenha.Adapter.RequimentRecyclerView;
import com.example.hethongthuenha.Adapter.RoomRequirementRecyclerView;
import com.example.hethongthuenha.Model.Person;
import com.example.hethongthuenha.Model.Requirement;
import com.example.hethongthuenha.Model.Room;
import com.example.hethongthuenha.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class fragment_requiment extends Fragment {
    private FloatingActionButton floatingActionButton;
    private EditText edPrice, edAddress, edDescription;
    private Spinner spTypeRoom;
    private Button btnFinish, btnContact, btnCancel;
    private TextView tvRequirementAdded, tvDescription;
    private ProgressDialog progressDialog;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference refRequirement = db.collection("Requirement").document();
    private Query refRoom = db.collection("Room").
            whereEqualTo("person_id", PersonAPI.getInstance().getUid());
    private RequimentRecyclerView adapter;
    private RecyclerView recyclerView;
    private List<Requirement> requirements;
    private List<Room> rooms;
    NumberFormat formatter;

    public fragment_requiment() {
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
        View view = inflater.inflate(R.layout.fragment_requiment, container, false);
        floatingActionButton = view.findViewById(R.id.floatingActionButton_requiment);
        formatter = NumberFormat.getCurrencyInstance();
        LoadRequirement();
        InitAdapter(view);
        progressDialog = new ProgressDialog(getActivity());
        floatingActionButton.setOnClickListener(v -> {
            AddRequirement();
        });
        return view;
    }

    private void LoadRequirement() {
        db.collection("Requirement").orderBy("requimentAdded", Query.Direction.DESCENDING).addSnapshotListener((value, error) -> {
            if (error == null) {
                requirements.clear();
                for (QueryDocumentSnapshot object : value) {
                    Requirement requirement = object.toObject(Requirement.class);
                    requirements.add(requirement);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void InitAdapter(View view) {
        recyclerView = view.findViewById(R.id.requimentRecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        requirements = new ArrayList<>();
        rooms = new ArrayList<>();
        adapter = new RequimentRecyclerView(getActivity(), requirements);
        adapter.setListenerEdit(requirement ->
                SelectUserChoose(requirement));
        adapter.setListenerCardView(requirement -> {
            ShowDialogContact(requirement);
        });
        recyclerView.setAdapter(adapter);
    }

    public AlertDialog ShowDialogRequirement() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View viewLayout = getLayoutInflater().inflate(R.layout.layout_create_requiment, null);
        builder.setView(viewLayout);
        edPrice = viewLayout.findViewById(R.id.ed_money_requiment);
        edAddress = viewLayout.findViewById(R.id.ed_address_requiment);
        edDescription = viewLayout.findViewById(R.id.ed_description_requiment);
        btnFinish = viewLayout.findViewById(R.id.btn_finish_requiment);
        spTypeRoom = viewLayout.findViewById(R.id.sp_type_room_requiment);


        //edDescription.setText("HAHAHHA TEST !!!");

        final AlertDialog show = builder.show();
        return show;
    }

    public void ShowDialogContact(Requirement requirement) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View viewLayout = getLayoutInflater().inflate(R.layout.layout_watch_requirement, null);
        builder.setView(viewLayout);
        tvRequirementAdded = viewLayout.findViewById(R.id.tv_added_requirement);
        tvDescription = viewLayout.findViewById(R.id.tv_description_requirement);
        btnCancel = viewLayout.findViewById(R.id.btn_cancel_requirement);
        btnContact = viewLayout.findViewById(R.id.btn_contact_requirement);

        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(requirement.getRequimentAdded()
                .getSeconds() * 1000);
        tvRequirementAdded.setText(timeAgo);
        tvDescription.setText(requirement.getDescription());

        final AlertDialog show = builder.show();

        btnCancel.setOnClickListener(v -> {
            show.dismiss();
        });
        btnContact.setOnClickListener(v -> {
            ShowDialogSelectRoom(requirement);
            show.dismiss();
        });
    }

    private void ShowDialogSelectRoom(Requirement requirement) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View viewLayout = getLayoutInflater().inflate(R.layout.layout_room_requiment, null);
        builder.setView(viewLayout);
        RecyclerView recyclerViewRoomRequirement = viewLayout.findViewById(R.id.recyclerViewRoomRequirement);
        ProgressBar progressDialogRoomRequirement = viewLayout.findViewById(R.id.progressBarRoomRequirement);
        Button btnBack = viewLayout.findViewById(R.id.btn_turn_off_room_requirement);
        recyclerViewRoomRequirement.setLayoutManager(new LinearLayoutManager(getActivity()));
        RoomRequirementRecyclerView adapter = new RoomRequirementRecyclerView(getActivity(), rooms);
        adapter.setListener(room ->{
            db.collection("User").whereEqualTo("uid", requirement.getId_person())
                    .get().addOnCompleteListener(value -> {
                if (value.isSuccessful()) {
                    for (QueryDocumentSnapshot persons : value.getResult()) {
                        Person person = persons.toObject(Person.class);
                        if(!requirement.getId_person().equals(PersonAPI.getInstance().getUid())){
                            Intent intent = new Intent(getActivity(), ActivityChat.class);
                            intent.putExtra("toId", person.getUid());
                            intent.putExtra("toEmail", person.getEmail());
                            intent.putExtra("toName", person.getFullName());
                            intent.putExtra("description_room", "Tôi có thể đáp ứng cho bạn " + room.getStage1().getTitle());
                            intent.putExtra("url", room.getStage3().getImagesURL().get(0));
                            startActivity(intent);
                        }
                    }
                }
            });
        });
        recyclerViewRoomRequirement.setAdapter(adapter);
        final AlertDialog show = builder.show();

        btnBack.setOnClickListener(v -> {
            show.dismiss();
        });

        refRoom.get().addOnCompleteListener(task -> {
            if (rooms.isEmpty()) {
                for (QueryDocumentSnapshot value : task.getResult()) {
                    rooms.add(value.toObject(Room.class));
                }
                if (task.isComplete()) {
                    adapter.notifyDataSetChanged();
                    progressDialogRoomRequirement.setVisibility(View.GONE);
                }

            } else {
                progressDialogRoomRequirement.setVisibility(View.GONE);
            }
        });
    }


    private void UpdateRequirement(Requirement requirement) {
        AlertDialog show = ShowDialogRequirement();
        edPrice.setText("" + requirement.getPrice());
        edDescription.setText(requirement.getDescription());
        edAddress.setText(requirement.getAddress());

        int indexSpinner = selectSpinnerItemByValue(spTypeRoom, requirement.getType_room());
        spTypeRoom.setSelection(indexSpinner);
        btnFinish.setText("Sửa");

        btnFinish.setOnClickListener(v -> {

            progressDialog.setMessage("Xin hãy chờ 1 lát");
            progressDialog.show();

            double price = Double.parseDouble(edPrice.getText().toString());
            String address = edAddress.getText().toString();
            String description = edDescription.getText().toString();
            String type_room = spTypeRoom.getSelectedItem().toString();

            requirement.setAddress(address);
            requirement.setDescription(description);
            requirement.setPrice(price);
            requirement.setType_room(type_room);


            db.collection("Requirement").whereEqualTo("id", requirement.getId())
                    .get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (rooms.isEmpty())
                        for (QueryDocumentSnapshot value : task.getResult()) {
                            db.collection("Requirement").document(value.getId())
                                    .set(requirement).addOnCompleteListener(x -> {
                                Toast.makeText(getActivity(), "Sửa thành công", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            });
                        }
                }
            });
            show.dismiss();
        });
    }

    private AlertDialog NotificationChooseDelete(Requirement requirement) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Thông báo xóa");
        builder.setMessage("Bạn có thật sự muốn xóa ?");

        builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());

        builder.setPositiveButton("Có", (dialog, which) -> DeleteRequirement(requirement));

        AlertDialog show = builder.show();

        return show;
    }

    private void SelectUserChoose(Requirement requirement) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setTitle("Lựa chọn của bạn");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item);
        arrayAdapter.add("Cập nhật yêu cầu");
        arrayAdapter.add("Xóa yêu cầu");

        builderSingle.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            switch (which) {
                case 0:
                    UpdateRequirement(requirement);
                    break;
                case 1:
                    NotificationChooseDelete(requirement);
                    break;
            }
        });
        builderSingle.show();
    }

    private void DeleteRequirement(Requirement requirement) {
        progressDialog.setMessage("Xin hãy chờ 1 lát");
        progressDialog.show();

        db.collection("Requirement").whereEqualTo("id", requirement.getId())
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot value : task.getResult()) {
                    db.collection("Requirement").document(value.getId())
                            .delete().addOnCompleteListener(v -> {
                        Toast.makeText(getActivity(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    });
                }
            }
        });
    }

    private int selectSpinnerItemByValue(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++)
            if (spinner.getItemAtPosition(i).equals(value))
                return i;
        return -1;
    }

    private void AddRequirement() {
        AlertDialog show = ShowDialogRequirement();
        btnFinish.setText("Thêm");
        btnFinish.setOnClickListener(v -> {

            progressDialog.setMessage("Xin hãy chờ 1 lát");
            progressDialog.show();


            double price = Double.parseDouble(edPrice.getText().toString());
            String address = edAddress.getText().toString();
            String description = edDescription.getText().toString();
            String type_room = spTypeRoom.getSelectedItem().toString();
            String uid = PersonAPI.getInstance().getUid();
            String id = refRequirement.getId();
            Timestamp requirementAdded = new Timestamp(new Date());

            Requirement requirement = new Requirement(uid, id, price, description, address, type_room, requirementAdded);
            db.collection("Requirement").add(requirement).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
                }
            });
            show.dismiss();
        });

    }
}