package sam.com.sam;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Steffi on 30.09.2017.
 */

public class LearnLanguagesAdapter extends RecyclerView.Adapter<LearnLanguagesAdapter.LearnViewHolder> {

    List<String> languagesList;

    public LearnLanguagesAdapter(List<String> languagesList) {
        this.languagesList = languagesList;
    }

    @Override
    public LearnViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //return null;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_item, parent, false);
        return new LearnViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LearnViewHolder holder, int position) {
        String language = languagesList.get(position);
        holder.languageTv.setText(language);
    }

    @Override
    public int getItemCount() {
        if(languagesList == null) {
            return 0;
        }
        return this.languagesList.size();
    }

    public class LearnViewHolder extends RecyclerView.ViewHolder {

        TextView languageTv;

        public LearnViewHolder(View itemView) {
            super(itemView);
            languageTv = (TextView) itemView.findViewById(R.id.language_tv);
        }
    }
}
