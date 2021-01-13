package com.example.hethongthuenha.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hethongthuenha.ActivityRoomDetail;
import com.example.hethongthuenha.Model.Person;
import com.example.hethongthuenha.Model.Report;
import com.example.hethongthuenha.Model.Room;
import com.example.hethongthuenha.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class ReportRecyclerView extends RecyclerView.Adapter<ReportRecyclerView.MyViewHolder> {

    private Context context;
    private List<Report> reports;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ReportRecyclerView(Context context, List<Report> reports) {
        this.context = context;
        this.reports = reports;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView tvNameRoom, tvTypeReport, tvDescriptionReport,tvEmail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cv_custom_report);
            tvNameRoom = itemView.findViewById(R.id.tv_custom_title_report);
            tvTypeReport = itemView.findViewById(R.id.tv_custom_type_report);
            tvEmail=itemView.findViewById(R.id.tv_custom_email_report);
            tvDescriptionReport = itemView.findViewById(R.id.tv_custom_description_report);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_report_admin, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Report report = reports.get(position);

        db.collection("Room").whereEqualTo("room_id", report.getId_room())
                .get().addOnCompleteListener(v -> {
            if (v.isSuccessful()) {
                for (QueryDocumentSnapshot value : v.getResult()) {
                    Room room = value.toObject(Room.class);
                    holder.tvNameRoom.setText(room.getStage1().getTitle());
                    holder.cardView.setOnClickListener(v1 -> {
                        Intent intent = new Intent(context, ActivityRoomDetail.class);
                        intent.putExtra("room", room);
                        context.startActivity(intent);
                    });
                }
            }
        });

        db.collection("User").whereEqualTo("uid", report.getId_person())
                .get().addOnCompleteListener(v -> {
            if (v.isSuccessful()) {
                for (QueryDocumentSnapshot value : v.getResult()) {
                    Person person = value.toObject(Person.class);
                    holder.tvEmail.setText(person.getEmail());
                }
            }
        });

        holder.tvTypeReport.setText(report.getType_report());
        if (report.getDescription() != null && !report.getDescription().equals("")){
            holder.tvDescriptionReport.setText(report.getDescription());
        }

        else {
            holder.tvDescriptionReport.setText("Không có nội dung !");
        }

        holder.cardView.setOnClickListener(v1 -> {
            Toast.makeText(context, "Phòng đã bị xóa hoặc bị lỗi !", Toast.LENGTH_SHORT).show();
        });

    }


    @Override
    public int getItemCount() {
        return reports.size();
    }


}
