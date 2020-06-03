package ch.noseryoung;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import java.text.SimpleDateFormat;
import java.util.List;

import ch.noseryoung.persistence.Pendenz;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private List<Pendenz> mDataset;
    private OnListItemClickListener onItemClickListener;


    // Provide a suitable constructor
    public RecyclerViewAdapter(List<Pendenz> myDataset, OnListItemClickListener onItemClickListener) {
        mDataset = myDataset;
        this.onItemClickListener = onItemClickListener;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_pendenz, parent, false);
        return new MyViewHolder(v, this.onItemClickListener);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.titleView.setText(mDataset.get(position).getTitle());
        SimpleDateFormat SDFormat = new SimpleDateFormat("MMM dd");
        holder.dateView.setText(SDFormat.format(mDataset.get(position).getDate()));
        switch (mDataset.get(position).getPriority()) {
            case 0:
                holder.dateView.setTextColor(holder.dateView.getResources().getColor(R.color.colorPrimaryDark));
                break;
            case 1:
                holder.dateView.setTextColor(holder.dateView.getResources().getColor(R.color.colorNormalPriority));
                break;
            case 2:
                holder.dateView.setTextColor(holder.dateView.getResources().getColor(R.color.colorHighPriority));
                break;
        }
        holder.descriptionView.setText(mDataset.get(position).getDescription());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnListItemClickListener onItemClickListener;


        // each data item is just a string in this case
        TextView titleView;
        TextView dateView;
        TextView descriptionView;

        MyViewHolder(View v, OnListItemClickListener onItemClickListener) {
            super(v);
            titleView = v.findViewById(R.id.list_item_title);
            dateView = v.findViewById(R.id.list_item_date);
            descriptionView = v.findViewById(R.id.list_item_description);
            this.onItemClickListener = onItemClickListener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnListItemClickListener {
        void onItemClick(int position);
    }
}
