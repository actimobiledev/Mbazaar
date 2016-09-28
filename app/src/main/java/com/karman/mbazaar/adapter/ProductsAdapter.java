package com.karman.mbazaar.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.karman.mbazaar.R;
import com.karman.mbazaar.model.Product;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Product> productList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;
        public MyViewHolder (View view) {
            super (view);
            title = (TextView) view.findViewById (R.id.title);
            count = (TextView) view.findViewById (R.id.count);
            thumbnail = (ImageView) view.findViewById (R.id.thumbnail);
            overflow = (ImageView) view.findViewById (R.id.overflow);
        }
    }

    public ProductsAdapter (Context mContext, List<Product> productList) {
        this.mContext = mContext;
        this.productList = productList;
    }

    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from (parent.getContext ())
                .inflate (R.layout.product_card, parent, false);

        return new MyViewHolder (itemView);
    }

    @Override
    public void onBindViewHolder (final MyViewHolder holder, int position) {
        Product product = productList.get (position);
        holder.title.setText (product.getName ());
//        holder.count.setText (product.getNumOfSongs () + " songs");

        // loading album cover using Glide library
        Glide.with (mContext).load (product.getImage_url ()).into (holder.thumbnail);

        holder.overflow.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                showPopupMenu (holder.overflow);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu (View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu (mContext, view);
        MenuInflater inflater = popup.getMenuInflater ();
        inflater.inflate (R.menu.menu_product, popup.getMenu ());
        popup.setOnMenuItemClickListener (new MyMenuItemClickListener ());
        popup.show ();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener () {
        }

        @Override
        public boolean onMenuItemClick (MenuItem menuItem) {
            switch (menuItem.getItemId ()) {
                case R.id.action_add_to_cart:
                    Toast.makeText (mContext, "Add to Cart", Toast.LENGTH_SHORT).show ();
                    return true;
                case R.id.action_add_to_wishlist:
                    Toast.makeText (mContext, "Add to Wishlist", Toast.LENGTH_SHORT).show ();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount () {
        return productList.size ();
    }
}