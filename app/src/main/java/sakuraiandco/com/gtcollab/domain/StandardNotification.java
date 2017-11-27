package sakuraiandco.com.gtcollab.domain;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import sakuraiandco.com.gtcollab.constants.Constants;

/**
 * Created by kaliq on 11/26/2017.
 */

@Data
public class StandardNotification extends Notification implements Parcelable {

    public static final String BASE_URL = Constants.BASE_URL + "/standard-notifications/";

    @Builder
    StandardNotification(int id, String title, String message, String messageExpanded, User creator, List<Integer> recipients, List<Integer> recipientsReadBy, DateTime timestamp) {
        super(id, title, message, messageExpanded, creator, recipients, recipientsReadBy, timestamp);
    }

    StandardNotification(Parcel in) {
        super(in);
    }

    public static final Creator<StandardNotification> CREATOR = new Creator<StandardNotification>() {
        @Override
        public StandardNotification createFromParcel(Parcel in) {
            return new StandardNotification(in);
        }
        @Override
        public StandardNotification[] newArray(int size) {
            return new StandardNotification[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }
}
