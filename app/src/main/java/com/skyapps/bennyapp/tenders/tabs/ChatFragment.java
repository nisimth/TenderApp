package com.skyapps.bennyapp.tenders.tabs;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.skyapps.bennyapp.MainActivity;
import com.skyapps.bennyapp.Objects.Chat;
import com.skyapps.bennyapp.R;


public class ChatFragment extends Fragment {
    String user, company;
    ProgressDialog mProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_chat, container, false);

        final ListView list = (ListView) view.findViewById(R.id.list);
        final ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1);
        list.setAdapter(adapter);

        Firebase.setAndroidContext(getContext());
        user = getContext().getSharedPreferences("BennyApp" , Context.MODE_PRIVATE).getString("username" , "");
        company =  getContext().getSharedPreferences("BennyApp" , Context.MODE_PRIVATE).getString("company" , "");
        String line = "https://tenders-83c71.firebaseio.com/chats/" + user + "/" + company;
        final Firebase ref1 = new Firebase(line);

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("אנא המתן...");
        mProgressDialog.show();

        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();



                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Chat post = postSnapshot.getValue(Chat.class);
                    String author = post.getAuthor();
                    String message = post.getMessage();
                    Chat q = new Chat(author, message);

                    adapter.add(q.getMessage() + ": " + q.getAuthor());

                }

                mProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });





        view.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String m = ((EditText) view.findViewById(R.id.messageInput)).getText().toString();

                if (m.equals("")){

                } else {
                    adapter.add(m);

                    Chat chat = new Chat(m, user);
                    ref1.push().setValue(chat);

                    ((EditText) view.findViewById(R.id.messageInput)).setText("");
                }
            }
        });

        return view;
    }

}
