package com.example.hethongthuenha.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hethongthuenha.API.PersonAPI;
import com.example.hethongthuenha.Model.Comment;
import com.example.hethongthuenha.Model.Person;
import com.example.hethongthuenha.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentRecyclerView extends RecyclerView.Adapter<CommentRecyclerView.MyViewHolder> {

    private Context context;
    private List<Comment> comments;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CommentRecyclerView(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_comment_room, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Comment comment = comments.get(position);

        db.collection("User").whereEqualTo("uid", comment.getId_person())
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                for (QueryDocumentSnapshot value : queryDocumentSnapshots) {
                    Person person = value.toObject(Person.class);

                    if (!person.getUrl().equals(""))
                        Picasso.with(context).load(person.getUrl())
                                .placeholder(R.drawable.ic_baseline_person_24).into(holder.imgAvatar);

                    holder.tvName.setText(person.getFullName());
                }
            }
        });

        holder.tvDesciprition.setText(comment.getText());
        String timeFuture = (String) DateUtils.getRelativeTimeSpanString(comment.getTime_added()
                .getSeconds() * 1000);
        holder.tvTimestamp.setText(timeFuture);
        if (comment.getId_person().equals(PersonAPI.getInstance().getUid()))
            holder.imgEdit.setVisibility(View.VISIBLE);
        else
            holder.imgEdit.setVisibility(View.GONE);
        holder.imgEdit.setOnClickListener(v -> ShowDialogChoose(comment));
    }

    private void ShowDialogChoose(Comment comment) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo !");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.select_dialog_item);
        adapter.add("Chỉnh sửa");
        adapter.add("Xóa");

        builder.setAdapter(adapter, (dialog, which) -> {
            switch (which) {
                case 0:
                    ShowDialogUpdateComment(comment);
                    break;
                case 1:
                    ShowDialogDelete(comment);
                    break;
            }
        });

        AlertDialog dialog = builder.show();
    }

    private void ShowDialogUpdateComment(Comment comment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_comment, null);

        builder.setView(viewLayout);

        Button send = viewLayout.findViewById(R.id.btn_send_comment);
        Button cancel = viewLayout.findViewById(R.id.btn_cancel_comment);
        EditText text = viewLayout.findViewById(R.id.ed_text_comment);

        final AlertDialog show = builder.show();


        db.collection("Comment").whereEqualTo("id", comment.getId())
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot value : task.getResult()) {
                    Comment comment1 = value.toObject(Comment.class);
                    text.setText(comment.getText());

                    send.setOnClickListener(v -> {
                        if (!TextUtils.isEmpty(text.toString())) {
                            comment1.setText(text.getText().toString());


                            db.collection("Comment").document(value.getId())
                                    .set(comment1).addOnCompleteListener(task1 -> {
                                Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
                                show.dismiss();
                            });
                        }

                    });
                    }
                }
            }).addOnFailureListener(e -> Toast.makeText(context,
                "Bình luận bị lỗi hoặc bị không có mạng", Toast.LENGTH_SHORT).show());

        cancel.setOnClickListener(v ->

        {
            show.dismiss();
        });
    }

    private void ShowDialogDelete(Comment comment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo");
        builder.setTitle("Bạn có chắc muốn xóa không ?");
        builder.setPositiveButton("Đồng ý", (dialog, which) ->
                db.collection("Comment").whereEqualTo("id", comment.getId())
                        .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot value : task.getResult()) {

                            db.collection("Comment").document(value.getId())
                                    .delete().addOnCompleteListener(task1 -> {
                                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            });

                        }
                    }
                }));

        builder.setNegativeButton("Hủy", ((dialog, which) -> dialog.dismiss()));

        builder.show();
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgAvatar, imgEdit;
        private TextView tvName, tvDesciprition, tvTimestamp;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAvatar = itemView.findViewById(R.id.img_custom_person_comment);
            tvName = itemView.findViewById(R.id.tv_custom_name_comment);
            tvDesciprition = itemView.findViewById(R.id.tv_description_comment);
            tvTimestamp = itemView.findViewById(R.id.tv_timestamp_comment);
            imgEdit = itemView.findViewById(R.id.img_edit_comment);
        }
    }
}
