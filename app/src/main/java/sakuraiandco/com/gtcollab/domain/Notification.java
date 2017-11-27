package sakuraiandco.com.gtcollab.domain;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by kaliq on 11/1/2017.
 */

@Data
@AllArgsConstructor
abstract class Notification extends Entity implements Parcelable {

    int id;
    String title;
    String message;
    String messageExpanded;
    User creator;
    List<Integer> recipients;
    List<Integer> recipientsReadBy;
    DateTime timestamp;

    Notification(Parcel in) {
        recipients = new ArrayList<>();
        recipientsReadBy = new ArrayList<>();

        id = in.readInt();
        title = in.readString();
        message = in.readString();
        messageExpanded = in.readString();
        creator = in.readParcelable(User.class.getClassLoader());
        timestamp = new DateTime(in.readString());
        in.readList(recipients, null);
        in.readList(recipientsReadBy, null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(message);
        dest.writeString(messageExpanded);
        dest.writeParcelable(creator, flags);
        dest.writeString(timestamp.toString());
        dest.writeList(recipients); // TODO: out of order - messy? better way?
        dest.writeList(recipientsReadBy); // TODO: out of order - messy? better way?
    }
}
