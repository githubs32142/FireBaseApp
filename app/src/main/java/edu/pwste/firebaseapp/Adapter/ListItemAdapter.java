package edu.pwste.firebaseapp.Adapter;


import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import edu.pwste.firebaseapp.MainActivity;
import edu.pwste.firebaseapp.Model.ToDo;
import edu.pwste.firebaseapp.R;

class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener{
    ItemClickListener itemClickListener ;
    TextView textTitle,textDescription;
    public ListItemViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
        textTitle=(TextView)itemView.findViewById(R.id.itemTitle);
        textDescription=(TextView)itemView.findViewById(R.id.itemDescription);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select the ation");
        contextMenu.add(0,0,getAdapterPosition(),"DELETE");
    }
}

public class ListItemAdapter extends RecyclerView.Adapter<ListItemViewHolder> {
    MainActivity mainActivity;
    List<ToDo> toDoList;

    public ListItemAdapter(MainActivity mainActivity, List<ToDo> toDoList) {
        this.mainActivity = mainActivity;
        this.toDoList = toDoList;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(mainActivity.getBaseContext());
        View view = layoutInflater.inflate(R.layout.list_item, parent,false);
        return  new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        mainActivity.titleMD.setText(toDoList.get(position).getTitle());
        mainActivity.descriptiom.setText(toDoList.get(position).getDescription());
        mainActivity.isUpdate =true;
        mainActivity.idUpdate=toDoList.get(position).getId();
        holder.textTitle.setText(toDoList.get(position).getTitle());
        holder.textDescription.setText(toDoList.get(position).getDescription());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClik) {
                mainActivity.titleMD.setText(toDoList.get(position).getTitle());
                mainActivity.descriptiom.setText(toDoList.get(position).getDescription());
                mainActivity.isUpdate =true;
                mainActivity.idUpdate=toDoList.get(position).getId();
            }
        });
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }
}
