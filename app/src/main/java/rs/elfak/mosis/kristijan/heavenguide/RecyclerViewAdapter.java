package rs.elfak.mosis.kristijan.heavenguide;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import rs.elfak.mosis.kristijan.heavenguide.data.model.SearchRecyclerItem;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> implements Filterable {

    private ArrayList<SearchRecyclerItem> mSearchRecyclerList;
    private ArrayList<SearchRecyclerItem> mSearchRecyclerListFull;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = (OnItemClickListener) listener;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTextView1;

        public RecyclerViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.search_recycler_imageView);
            mTextView1 = itemView.findViewById(R.id.search_recycler_textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public RecyclerViewAdapter(ArrayList<SearchRecyclerItem> itemArrayList) {
        mSearchRecyclerList = itemArrayList;
        mSearchRecyclerListFull = new ArrayList<>(itemArrayList);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_recycler_item, parent, false);
        RecyclerViewHolder evh = new RecyclerViewHolder(v, (OnItemClickListener) mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        SearchRecyclerItem currentItem = mSearchRecyclerList.get(position);
        Bitmap picture = currentItem.getImageResource();
        holder.mImageView.setImageBitmap(Bitmap.createScaledBitmap((Bitmap) picture,  80, 80,false));
        holder.mTextView1.setText(currentItem.getText1());
    }

    @Override
    public int getItemCount() {
        return mSearchRecyclerList.size();
    }

    public void filterList(ArrayList<SearchRecyclerItem> filteredList){
        mSearchRecyclerList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<SearchRecyclerItem> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(mSearchRecyclerListFull);
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (SearchRecyclerItem item : mSearchRecyclerListFull) {
                    if (item.getText1().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mSearchRecyclerList.clear();
            mSearchRecyclerList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}
