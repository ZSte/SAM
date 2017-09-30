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

public class SpokenLanguagesAdapter extends RecyclerView.Adapter<SpokenLanguagesAdapter.SpokenViewHolder> {

    List<String> languagesList;

    public SpokenLanguagesAdapter(List<String> languagesList) {
        this.languagesList = languagesList;
    }

    @Override
    public SpokenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //return null;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_item, parent, false);
        return new SpokenViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SpokenViewHolder holder, int position) {
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

    public class SpokenViewHolder extends RecyclerView.ViewHolder {

        TextView languageTv;

        public SpokenViewHolder(View itemView) {
            super(itemView);
            languageTv = (TextView) itemView.findViewById(R.id.checkbox/*language_tv*/);
        }
    }
}
