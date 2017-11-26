package sakuraiandco.com.gtcollab.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import sakuraiandco.com.gtcollab.R;
import sakuraiandco.com.gtcollab.adapters.base.BaseAdapter;
import sakuraiandco.com.gtcollab.adapters.base.BaseViewHolder;
import sakuraiandco.com.gtcollab.domain.Group;
import sakuraiandco.com.gtcollab.domain.GroupMessage;
import sakuraiandco.com.gtcollab.domain.User;

public class GroupMessageAdapter extends BaseAdapter<GroupMessage, GroupMessageAdapter.GroupMessageViewHolder> {

    private static final int MESSAGE_TYPE_RECEIVED = 0;
    private static final int MESSAGE_TYPE_SENT = 1;

    private User user;

    public GroupMessageAdapter(User user) {
        this(new ArrayList<GroupMessage>(), user);
    }

    public GroupMessageAdapter(List<GroupMessage> data, User user) {
        super(data, null);
        this.user = user;
    }

    @Override
    public GroupMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("log", "create view holder");
        View view;
        if (viewType == MESSAGE_TYPE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new GroupMessageViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }
//
//    @Override
//    public void onBindViewHolder(GroupMessageViewHolder holder, int position) {
//        GroupMessage message = data.get(position);
//        // TODO: check if getItemViewType needed
//        holder.bind(message);
//    }

    @Override
    public int getItemViewType(int position) {
        GroupMessage message = data.get(position);
        if (message.getCreator().getId() == user.getId()) {
            return MESSAGE_TYPE_SENT;
        } else {
            return MESSAGE_TYPE_RECEIVED;
        }
    }

    class GroupMessageViewHolder extends BaseViewHolder<GroupMessage> {

        TextView messageText;
        TextView timeText;
        Context context;

        GroupMessageViewHolder(View view) {
            super(view);
            messageText = view.findViewById(R.id.text_message_body);
            timeText = view.findViewById(R.id.text_message_time);
            context = view.getContext();
        }

        public void bind(GroupMessage message) {
            messageText.setText(message.getContent());
            timeText.setText(formatDate(message.getTimestamp()));
        }

        private String formatDate(DateTime dateTime) {
            return DateFormat.getDateFormat(context).format(dateTime);
        }
    }

    class ReceivedMessageViewHolder extends GroupMessageViewHolder {

        TextView senderText;
        TextView avatarText;

        ReceivedMessageViewHolder(View v) {
            super(v);
            senderText = itemView.findViewById(R.id.text_message_sender);
            avatarText = itemView.findViewById(R.id.text_sender_avatar);
        }

        public void bind(GroupMessage message) {
            super.bind(message);
            User sender = message.getCreator();
            senderText.setText(sender.getFullName());
            avatarText.setText(formatName(sender.getFirstName(), sender.getLastName()));
        }

        private String formatName(String firstName, String lastName) {
            return ("" + firstName.charAt(0) + lastName.charAt(0)).toUpperCase();
        }
    }

}
